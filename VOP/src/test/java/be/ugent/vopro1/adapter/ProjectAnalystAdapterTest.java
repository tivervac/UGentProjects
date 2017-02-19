package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.DBInvalidQueryResult;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.mocks.MockHeaderAuthenticationHandler;
import be.ugent.vopro1.interactor.mocks.ProjectInteractorMock;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.AuthenticationHandler;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandlerImpl;
import be.ugent.vopro1.interactor.mocks.ProjectPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.util.ApplicationContextProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class ProjectAnalystAdapterTest {

    private ProjectAnalystAdapter adapter;
    private ProjectInteractorMock mockInteractor;
    private MockHeaderAuthenticationHandler mockAuthHandler;
    private ProjectPermissionMock mockPermissionHandler;

    /**
     * Initializes the adapter and injects the mock interactor
     */
    @Before
    public void initialize() {
        mockInteractor = new ProjectInteractorMock();
        ProjectInteractorFactory.setInstance(mockInteractor);
        mockAuthHandler = new MockHeaderAuthenticationHandler();
        mockPermissionHandler = new ProjectPermissionMock();
        PermissionProvider.set("project", mockPermissionHandler);

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", mockAuthHandler);
        AuthenticationFactory.setInstances(authMap);

        adapter = new ProjectAnalystAdapter();
    }

    @After
    public void tearDown() {
        ProjectInteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("projectInteractor", ProjectInteractor.class));

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", new HeaderAuthenticationHandlerImpl());

        AuthenticationFactory.setInstances(authMap);
    }

    @Test
    public void testGet() {
        adapter.get(new HashMap<>());
    }

    @Test
    public void testGetAll() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.getAll(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermissionHandler.setAllow(false);
        args.put("auth", "blah");
        args.put("projectName", "project");
        r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, projectName
        mockPermissionHandler.setAllow(true);
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
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermissionHandler.setAllow(false);
        args.put("auth", "blah");
        args.put("projectName", "project");
        args.put("userId", "1");
        r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No work load
        mockPermissionHandler.setAllow(true);
        r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, projectName, userId
        mockPermissionHandler.setAllow(true);
        args.put("work", "{\"work\": 10}");
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test
    public void testRemove() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        Result r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermissionHandler.setAllow(false);
        args.put("auth", "blah");
        args.put("projectName", "project");
        args.put("userId", "1");
        r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, projectName, userId
        mockPermissionHandler.setAllow(true);
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test
    public void testEdit() {
        adapter.edit(new HashMap<>());
    }
}