package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.DBAccessFailedResult;
import be.ugent.vopro1.adapter.result.types.DBInvalidQueryResult;
import be.ugent.vopro1.adapter.result.types.DBTempFailedResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.mocks.EntityInteractorMock;
import be.ugent.vopro1.interactor.mocks.EntityPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.util.ApplicationContextProvider;
import org.aikodi.lang.funky.executors.Actor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class EntityAdapterTest {

    private EntityAdapter adapter;
    private EntityPermissionMock mockPermissionHandler;
    private EntityInteractorMock<Actor> mockInteractor;

    /**
     * Initializes the adapter and injects the mock interactor
     */
    @Before
    public void initialize() {
        mockInteractor = new EntityInteractorMock<>();
        InteractorFactory.setInstance(mockInteractor);

        mockPermissionHandler = new EntityPermissionMock();
        PermissionProvider.set("entity", mockPermissionHandler);

        adapter = new EntityAdapter<>(Actor.class, "actor");
    }

    @After
    public void tearDown() {
        InteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("entityInteractor", EntityInteractor.class));
    }

    @Test
    public void testAdd() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        // Entered no actor name!
        args.put("projectName", "project");
        r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("blob", "{\"name\": \"actor\"}");
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertEquals("actor", ((Actor) r.getContent()).name());

        mockInteractor.setThrowing(new RecoverableDataAccessException(""));
        r = adapter.add(args);
        assertEquals(DBAccessFailedResult.class, r.getResultType());

        mockInteractor.setThrowing(new NonTransientDataAccessResourceException(""));
        r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        mockInteractor.setThrowing(new TransientDataAccessResourceException(""));
        r = adapter.add(args);
        assertEquals(DBTempFailedResult.class, r.getResultType());
    }

    @Test
    public void testRemove() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        // Entered no actor name!
        args.put("projectName", "project");
        r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("name", "actor");
        r = adapter.remove(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertNull(r.getContent());

        mockInteractor.setThrowing(new RecoverableDataAccessException(""));
        r = adapter.remove(args);
        assertEquals(DBAccessFailedResult.class, r.getResultType());

        mockInteractor.setThrowing(new NonTransientDataAccessResourceException(""));
        r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        mockInteractor.setThrowing(new TransientDataAccessResourceException(""));
        r = adapter.remove(args);
        assertEquals(DBTempFailedResult.class, r.getResultType());
    }

    @Test
    public void testGet() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        // Entered no actor name!
        args.put("projectName", "project");
        r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("name", "actor");
        r = adapter.get(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertEquals("actor", ((Actor) r.getContent()).name());

        mockInteractor.setThrowing(new RecoverableDataAccessException(""));
        r = adapter.get(args);
        assertEquals(DBAccessFailedResult.class, r.getResultType());

        mockInteractor.setThrowing(new NonTransientDataAccessResourceException(""));
        r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        mockInteractor.setThrowing(new TransientDataAccessResourceException(""));
        r = adapter.get(args);
        assertEquals(DBTempFailedResult.class, r.getResultType());
    }

    @Test
    public void testGetAll() throws Exception {
        HashMap<String, String> args = new HashMap<>();
        ArrayList<Actor> list;

        args.put("type", "actor");
        args.put("projectName", "project");
        Result r = adapter.getAll(args);
        assertEquals(SuccessResult.class, r.getResultType());
        list = (ArrayList<Actor>) r.getContent();
        assertEquals(1, list.size());

        mockInteractor.setThrowing(new RecoverableDataAccessException(""));
        r = adapter.getAll(args);
        assertEquals(DBAccessFailedResult.class, r.getResultType());

        mockInteractor.setThrowing(new NonTransientDataAccessResourceException(""));
        r = adapter.getAll(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        mockInteractor.setThrowing(new TransientDataAccessResourceException(""));
        r = adapter.getAll(args);
        assertEquals(DBTempFailedResult.class, r.getResultType());
    }

    @Test
    public void testEdit() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.edit(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        // Entered no actor name!
        args.put("projectName", "project");
        r = adapter.edit(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("name", "actor");
        args.put("blob", "{ \"name\" : \"actor_renamed\" }");
        r = adapter.edit(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertEquals("actor_renamed", ((Actor) r.getContent()).name());

        mockInteractor.setThrowing(new RecoverableDataAccessException(""));
        r = adapter.edit(args);
        assertEquals(DBAccessFailedResult.class, r.getResultType());

        mockInteractor.setThrowing(new NonTransientDataAccessResourceException(""));
        r = adapter.edit(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        mockInteractor.setThrowing(new TransientDataAccessResourceException(""));
        r = adapter.edit(args);
        assertEquals(DBTempFailedResult.class, r.getResultType());
    }
}