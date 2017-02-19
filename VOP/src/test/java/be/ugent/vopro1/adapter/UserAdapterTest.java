package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.*;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.AuthenticationHandler;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandlerImpl;
import be.ugent.vopro1.interactor.mocks.MockHeaderAuthenticationHandler;
import be.ugent.vopro1.interactor.mocks.UserInteractorMock;
import be.ugent.vopro1.interactor.mocks.UserPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.interactor.user.UserInteractorFactory;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.util.ApplicationContextProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class UserAdapterTest {

    private UserAdapter adapter;
    private UserInteractorMock mockInteractor;
    private UserPermissionMock mockPermission;
    private MockHeaderAuthenticationHandler mockAuthHandler;

    /**
     * Initializes the adapter and injects the mock interactor
     */
    @Before
    public void initialize() {
        mockInteractor = new UserInteractorMock();
        UserInteractorFactory.setInstance(mockInteractor);
        mockAuthHandler = new MockHeaderAuthenticationHandler();
        mockPermission = new UserPermissionMock();
        PermissionProvider.set("user", mockPermission);

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", mockAuthHandler);
        AuthenticationFactory.setInstances(authMap);

        adapter = new UserAdapter();
    }

    @After
    public void tearDown() {
        UserInteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("userInteractor", UserInteractor.class));

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", new HeaderAuthenticationHandlerImpl());

        AuthenticationFactory.setInstances(authMap);
        PermissionProvider.setDefault();
    }

    @Test
    public void testGet() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermission.setAllow(false);
        args.put("auth", "blah");
        r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, no userId
        mockPermission.setAllow(true);
        r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, userId
        args.put("id", "1");
        r = adapter.get(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());
    }

    @Test
    public void testGetAll() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermission.setAllow(false);
        args.put("auth", "blah");
        r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, no page
        mockPermission.setAllow(true);
        r = adapter.getAll(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());

        // Permission, page
        args.put("page", "1");
        r = adapter.getAll(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());
    }

    @Test
    public void testAdd() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermission.setAllow(false);
        args.put("auth", "blah");
        args.put("blob", "{\"email\": \"me@gmail.com\"}");
        r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, no blob
        mockPermission.setAllow(true);
        args.remove("blob");
        r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, Wrong blob
        mockPermission.setAllow(true);
        args.put("blob", "{\"eail\" \"me@gmail.com\"}");
        r = adapter.add(args);
        assertEquals(JSONParseErrorResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, blob
        mockPermission.setAllow(true);
        args.remove("blob");
        args.put("blob", "{\"email\": \"me@gmail.com\"}");
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());
        assertThat(r.getContent(), instanceOf(User.class));
        assertEquals("me@gmail.com", ((User) r.getContent()).getEmail());
    }

    @Test
    public void testRemove() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermission.setAllow(false);
        args.put("auth", "blah");
        r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, userId
        args.put("id", "1");
        mockPermission.setAllow(true);
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test
    public void testEdit() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermission.setAllow(false);
        args.put("auth", "blah");
        r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, userId
        args.put("id", "1");
        args.put("blob", "{\"email\": \"me2@gmail.com\"}");
        mockPermission.setAllow(true);
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }
}