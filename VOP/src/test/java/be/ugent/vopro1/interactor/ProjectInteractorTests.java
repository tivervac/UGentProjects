package be.ugent.vopro1.interactor;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.funky.mocks.CustomWorkspaceMock;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorImpl;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.ProjectDAOFactory;
import be.ugent.vopro1.persistence.factory.UserDAOFactory;
import be.ugent.vopro1.persistence.jdbc.mocks.ProjectDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOProjectMock;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.aikodi.chameleon.workspace.LanguageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class ProjectInteractorTests {

    private ProjectInteractor interactor;
    private ProjectDAOMock daoMock;
    private UserDAOProjectMock userDAOProjectMock;
    private CustomWorkspaceMock workspaceMock;

    @Before
    public void setUp() throws Exception {
        workspaceMock = new CustomWorkspaceMock(new LanguageRepository());
        WorkspaceFactory.setInstance(workspaceMock);

        daoMock = new ProjectDAOMock();
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        projectDAOFactory.setInstance(daoMock);
        DAOProvider.setInstance("project", projectDAOFactory);
        userDAOProjectMock = new UserDAOProjectMock();
        UserDAOFactory userDAOFactory = new UserDAOFactory();
        userDAOFactory.setInstance(userDAOProjectMock);
        DAOProvider.setInstance("user", userDAOFactory);


        interactor = new ProjectInteractorImpl();
    }

    @After
    public void tearDown() throws Exception {
        WorkspaceFactory.setDefault();
        DAOProvider.setDefault();
    }

    @Test
    public void testAddProject() throws Exception {
        EntityProject beg = new EntityProject("testproject");
        beg.setLeader(User.UserBuilder.aUser().email("test@test.com").build());
        EntityProject res = interactor.addProject(beg);

        assertNotNull(res);
        assertEquals("testproject", res.getName());
        assertTrue(workspaceMock.getProjects().contains("testproject"));
    }

    @Test
    public void testEditProject() throws Exception {
        WorkspaceFactory.setInstance(workspaceMock);
        interactor = new ProjectInteractorImpl();
        daoMock.save(new PersistentProject("testproject", 1));

        EntityProject old = new EntityProject("testproject");
        workspaceMock.createProject("testproject");
        EntityProject upd = new EntityProject("testproject-renamed");
        upd.setLeader(User.UserBuilder.aUser().email("test@test.com").build());

        EntityProject res = interactor.editProject(old, upd);

        assertNotNull(res);
        assertEquals("testproject-renamed", res.getName());
    }

    @Test
    public void testGetProject() throws Exception {
        daoMock.save(new PersistentProject("testproject", 1));
        EntityProject get = new EntityProject("testproject");
        EntityProject res = interactor.getProject(get);

        assertNotNull(res);
        assertEquals("testproject", res.getName());
    }

    @Test
    public void testGetAllProjects() throws Exception {
        List<EntityProject> res = interactor.getAllProjects(false);

        assertNotNull(res);
        assertEquals(2, res.size());
    }

    @Test
    public void testFindProjects() throws Exception {
        List<EntityProject> res = interactor.findProjects("name1");
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("name1", res.get(0).getName());

        res = interactor.findProjects("names");
        assertNotNull(res);
        assertEquals(0, res.size());
    }

    @Ignore("This method is currently failing because Funky isn't very testable")
    @Test
    public void testRemoveProject() throws Exception {
        EntityProject beg = new EntityProject("testproject");
        interactor.addProject(beg);

        assertEquals(1, workspaceMock.getProjects().size());

        interactor.removeProject(beg);

        assertEquals(0, workspaceMock.getProjects().size());
    }

    @Test
    public void testGetTeams() throws Exception {
        List<Team> res = interactor.getTeams(1);

        assertNotNull(res);
        assertEquals(1, res.size());
    }

    @Test
    public void testGetAnalysts() throws Exception {
        daoMock.save(new PersistentProject("testproject", 1));
        List<AvailableUser> res = interactor.getAnalysts("testproject");

        assertNotNull(res);
        assertEquals(1, res.size());
        assertNotNull(res.get(0).getUser());
    }

    //TODO: test workload
    @Test(expected = RequirementNotMetException.class)
    public void testAddAnalyst() throws Exception {
        interactor.addAnalyst("testproject", 5, 10);

        assertEquals(1, daoMock.getAnalysts().size());
        assertEquals(new Integer(5), daoMock.getAnalysts().get(0));
    }

    @Test(expected = RequirementNotMetException.class)
    public void testRemoveAnalyst() throws Exception {
        interactor.addAnalyst("testproject", 5, 10);

        assertEquals(1, daoMock.getAnalysts().size());

        interactor.removeAnalyst("testproject", 5);

        assertEquals(0, daoMock.getAnalysts().size());
    }
}
