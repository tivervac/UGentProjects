package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.DBAccessFailedResult;
import be.ugent.vopro1.adapter.result.types.DBInvalidQueryResult;
import be.ugent.vopro1.adapter.result.types.DBTempFailedResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.mocks.EntityInteractorMock;
import be.ugent.vopro1.interactor.mocks.ProjectInteractorMock;
import be.ugent.vopro1.interactor.mocks.ProjectPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.util.ApplicationContextProvider;
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
public class ProjectAdapterTest {

    private ProjectAdapter adapter;
    private ProjectPermissionMock mockPermissionHandler;
    private EntityInteractorMock mockEntityInteractor;
    private ProjectInteractorMock mockInteractor;

    /**
     * Initializes the adapter and injects the mock interactor
     */
    @Before
    public void initialize() {
        mockInteractor = new ProjectInteractorMock();
        mockEntityInteractor= new EntityInteractorMock<>();
        ProjectInteractorFactory.setInstance(mockInteractor);
        InteractorFactory.setInstance(mockEntityInteractor);
        mockPermissionHandler = new ProjectPermissionMock();
        PermissionProvider.set("project", mockPermissionHandler);

        adapter = new ProjectAdapter();
    }

    @After
    public void tearDown() {
        ProjectInteractorFactory.setInstance(
                ApplicationContextProvider.getApplicationContext().getBean("projectInteractor", ProjectInteractor.class));
    }

    @Test
    public void testAdd() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.add(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("blob", "{\"name\": \"project\"}");
        r = adapter.add(args);
        assertEquals(SuccessResult.class, r.getResultType());

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
    public void testRemoveProject() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.remove(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("projectName", "project");
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
    public void testGetProject() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.get(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("projectName", "project");
        r = adapter.get(args);
        assertEquals(SuccessResult.class, r.getResultType());

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
    public void testGetAllProjects() throws Exception {
        HashMap<String, String> args = new HashMap<>();
        ArrayList<EntityProject> list;

        // No name
        Result r = adapter.getAll(args);
        assertEquals(SuccessResult.class, r.getResultType());
        list = (ArrayList<EntityProject>) r.getContent();
        assertEquals(2, list.size());

        args.put("name", "project");
        r = adapter.getAll(args);
        assertEquals(SuccessResult.class, r.getResultType());
        list = (ArrayList<EntityProject>) r.getContent();
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
    public void testEditProject() throws Exception {
        HashMap<String, String> args = new HashMap<>();

        // Empty arguments!
        Result r = adapter.edit(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("blob", "{\"name\": \"newproject\"}");

        // Still not enough arguments
        r = adapter.edit(args);
        assertEquals(DBInvalidQueryResult.class, r.getResultType());

        args.put("projectName", "oldproject");
        
        r = adapter.edit(args);
        assertEquals(SuccessResult.class, r.getResultType());
        assertEquals("newproject", ((EntityProject) r.getContent()).getName());

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

    @Test
    public void testType() {
        assertEquals(EntityProject.class, adapter.getType());
    }
}
