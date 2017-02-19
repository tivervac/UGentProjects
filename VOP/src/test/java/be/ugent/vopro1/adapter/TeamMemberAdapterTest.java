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
import be.ugent.vopro1.interactor.team.TeamPermission;
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
public class TeamMemberAdapterTest {

    private TeamMemberAdapter adapter;
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

        adapter = new TeamMemberAdapter();
    }

    @After
    public void tearDown() {
        TeamInteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("teamInteractor", TeamInteractor.class));

        HashMap<String, AuthenticationHandler> authMap = new HashMap<>();
        authMap.put("header", new HeaderAuthenticationHandlerImpl());

        AuthenticationFactory.setInstances(authMap);
    }

    @Test(expected = RuntimeException.class)
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
        args.put("teamId", "1");
        r = adapter.getAll(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, projectName
        mockPermissionHandler.setAllow(true);
        args.put("teamId", "1");
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
        args.put("teamId", "1");
        args.put("userId", "1");
        args.put("auth", "blah");
        r = adapter.add(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, teamId, userId
        mockPermissionHandler.setAllow(true);
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test
    public void testRemove() {
        HashMap<String, String> args = new HashMap<>();

        // No header
        args.put("teamId", "1");
        args.put("userId", "1");
        Result r = adapter.remove(args);
        assertEquals(NoPermissionResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, no teamId, no userId
        mockPermissionHandler.setAllow(true);
        args.remove("teamId");
        args.remove("userId");
        r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());
        assertFalse(r.isSuccessful());

        // Permission, projectName, userId
        args.put("teamId", "1");
        args.put("userId", "1");
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertTrue(r.isSuccessful());
        assertNull(r.getContent());
    }

    @Test(expected = RuntimeException.class)
    public void testEdit() {
        adapter.edit(new HashMap<>());
    }
}