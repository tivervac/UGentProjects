package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.DBInvalidQueryResult;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.mocks.MockHeaderAuthenticationHandler;
import be.ugent.vopro1.interactor.mocks.TeamInteractorMock;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.AuthenticationHandler;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandlerImpl;
import be.ugent.vopro1.interactor.mocks.TeamPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.interactor.team.TeamInteractorFactory;
import be.ugent.vopro1.model.Team;
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
public class TeamAdapterTest {

    private TeamAdapter adapter;
    private TeamInteractorMock mockInteractor;
    private MockHeaderAuthenticationHandler mockAuthHandler;
    private TeamPermissionMock mockPermissionHandler;
    
    /**
     * Initializes the adapter and injects the mock interactor
     */
    @Before
    public void initialize() {
        mockInteractor = new TeamInteractorMock();
        TeamInteractorFactory.setInstance(mockInteractor);
        mockAuthHandler = new MockHeaderAuthenticationHandler();
        mockPermissionHandler = new TeamPermissionMock();

        PermissionProvider.set("team", mockPermissionHandler);
        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", mockAuthHandler);
        AuthenticationFactory.setInstances(authMap);

        adapter = new TeamAdapter();
    }

    @After
    public void tearDown() {
        TeamInteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("teamInteractor", TeamInteractor.class));

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", new HeaderAuthenticationHandlerImpl());

        AuthenticationFactory.setInstances(authMap);
    }

    @Test
    public void testGet() {
        HashMap<String, String> args = new HashMap<>();

        // Empty request
        Result r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // teamId, no auth
        args.put("teamId", "1");
        mockPermissionHandler.setAllow(false);
        r = adapter.get(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        args.put("auth", "blah");
        r = adapter.get(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, no teamId
        mockPermissionHandler.setAllow(true);
        r = adapter.get(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());

    }

    @Test
    public void testGetAll() {
        HashMap<String, String> args = new HashMap<>();

        // Empty request
        Result r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // teamId, no auth
        args.put("teamId", "1");
        mockPermissionHandler.setAllow(false);
        r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        args.put("auth", "blah");
        r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission
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
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission, no auth header
        args.put("blob", "{\"name\": \"testteam\"}");
        mockPermissionHandler.setAllow(false);
        r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        args.put("auth", "blah");
        r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, blob
        mockPermissionHandler.setAllow(true);
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNotNull(r.getContent());
        assertThat(r.getContent(), instanceOf(Team.class));
        assertEquals("testteam", ((Team) r.getContent()).getName());
    }

    @Test
    public void testRemove() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        args.put("teamId", "1");
        Result r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermissionHandler.setAllow(false);
        args.put("auth", "blah");
        r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, teamId
        args.put("teamId", "1");
        mockPermissionHandler.setAllow(true);
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test
    public void testEdit() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        args.put("teamId", "1");
        Result r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // No permission
        mockPermissionHandler.setAllow(false);
        args.put("auth", "blah");
        r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, teamId
        args.put("blob", "{\"name\": \"testteam-renamed\"}");
        mockPermissionHandler.setAllow(true);
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }
}