package be.ugent.vopro1.interactor;

import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.interactor.team.TeamInteractorImpl;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.ProjectDAOFactory;
import be.ugent.vopro1.persistence.factory.TeamDAOFactory;
import be.ugent.vopro1.persistence.factory.UserDAOFactory;
import be.ugent.vopro1.persistence.jdbc.mocks.ProjectDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.TeamDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOProjectMock;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class TeamInteractorTests {

    private TeamDAOMock daoMock;
    private ProjectDAOMock projectDAOMock;
    private UserDAOProjectMock userDAOMock;
    private TeamInteractor interactor;
    
    @Before
    public void setUp() throws Exception {
        daoMock = new TeamDAOMock();
        TeamDAOFactory teamDAOFactory = new TeamDAOFactory();
        teamDAOFactory.setInstance(daoMock);
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        projectDAOMock = new ProjectDAOMock();
        projectDAOFactory.setInstance(projectDAOMock);
        UserDAOFactory userDAOFactory = new UserDAOFactory();
        userDAOMock = new UserDAOProjectMock();
        userDAOFactory.setInstance(userDAOMock);
        DAOProvider.setInstance("project", projectDAOFactory);
        DAOProvider.setInstance("team", teamDAOFactory);
        DAOProvider.setInstance("user", userDAOFactory);
        interactor = new TeamInteractorImpl();

        Team team = Team.TeamBuilder.aTeam().name("team").leader(User.UserBuilder.aUser().email("test@test.com").build()).build();
        interactor.addTeam(team);
    }

    @After
    public void tearDown() throws Exception {
        DAOProvider.setDefault();
    }

    @Test
    public void testAddTeam() throws Exception {
        Team in = Team.TeamBuilder.aTeam().name("newteam").leader(User.UserBuilder.aUser().email("test@test.com").build()).build();
        Team out = interactor.addTeam(in);

        assertNotNull(out);
        assertEquals("newteam", out.getName());
    }

    @Test
    public void testEditTeam() throws Exception {
        Team in = Team.TeamBuilder.aTeam().name("team-renamed").leader(User.UserBuilder.aUser().email("test@test.com").build()).build();
        Team out = interactor.editTeam(1, in);

        assertNotNull(out);
        assertEquals("team-renamed", out.getName());
    }

    @Test
    public void testGetTeam() throws Exception {
        Team out = interactor.getTeam(1);

        assertNotNull(out);
        assertEquals("team", out.getName());
    }

    @Test
    public void testGetTeam1() throws Exception {
        Team out = interactor.getTeam("team");

        assertNotNull(out);
        assertEquals("team", out.getName());
    }

    @Test
    public void testGetAllTeams() throws Exception {
        List<Team> teams = interactor.getAllTeams();

        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test(expected = RequirementNotMetException.class)
    public void testRemoveTeam() throws Exception {
        interactor.removeTeam(1);

        // Throws NPE
        interactor.getTeam(1);
    }

    @Test
    public void testGetTeamMembers() throws Exception {
        interactor.addTeamMember(1, 1);
        interactor.addTeamMember(1, 2);

        List<User> members = interactor.getTeamMembers(1, false);
        
        assertNotNull(members);
        assertEquals(2, members.size());
    }

    @Test
    public void testAddTeamMember() throws Exception {
        assertEquals(0, interactor.getTeamMembers(1, false).size());
        
        interactor.addTeamMember(1, 1);
        
        assertEquals(1, interactor.getTeamMembers(1, false).size());
    }

    @Test
    public void testRemoveTeamMember() throws Exception {
        interactor.addTeamMember(1, 1);
        interactor.addTeamMember(1, 2);

        List<User> members = interactor.getTeamMembers(1, false);

        assertEquals(2, members.size());

        interactor.removeTeamMember(1, 2);
        
        members = interactor.getTeamMembers(1, false);
        
        assertEquals(1, members.size());
    }

    @Test
    public void testGetProjects() throws Exception {
        projectDAOMock.setTeams(new ArrayList<>());
        interactor.addTeamProject(1, "project3");

        List<EntityProject> members = interactor.getProjects(1);

        assertNotNull(members);
        assertEquals(1, members.size());
    }

    @Test
    public void testAddTeamProject() throws Exception {
        List<EntityProject> proj = interactor.getProjects(1);
        assertEquals(0, proj.size());
        projectDAOMock.setTeams(new ArrayList<>());

        interactor.addTeamProject(1, "project5");
        proj = interactor.getProjects(1);

        assertEquals(1, proj.size());
    }

    @Test
    public void testRemoveTeamProject() throws Exception {
        List<EntityProject> proj = interactor.getProjects(1);
        assertEquals(0, proj.size());
        projectDAOMock.setTeams(new ArrayList<>());

        interactor.addTeamProject(1, "project6");
        proj = interactor.getProjects(1);

        assertEquals(1, proj.size());

        interactor.removeTeamProject(1, "name");
        proj = interactor.getProjects(1);

        assertEquals(0, proj.size());
    }
}