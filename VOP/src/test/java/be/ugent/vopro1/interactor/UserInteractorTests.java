package be.ugent.vopro1.interactor;

import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.interactor.user.UserInteractorImpl;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.UserDAOFactory;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOProjectMock;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class UserInteractorTests {

    private UserInteractor interactor;
    private UserDAOMock daoMock;

    @Before
    public void setUp() throws Exception {
        daoMock = new UserDAOMock();
        UserDAOFactory userDAOFactory = new UserDAOFactory();
        userDAOFactory.setInstance(daoMock);
        DAOProvider.setInstance("user", userDAOFactory);

        interactor = new UserInteractorImpl();

        User user = User.UserBuilder.aUser().email("me@example.com").build();
        interactor.addUser(user);
    }

    @After
    public void tearDown() throws Exception {
        DAOProvider.setDefault();
    }

    @Test
    public void testAddUser() throws Exception {
        User user = User.UserBuilder.aUser().email("me2@example.com").build();
        User ret = interactor.addUser(user);

        assertNotNull(ret);
        assertEquals("me2@example.com", ret.getEmail());
    }

    @Test
    public void testEditUser() throws Exception {
        User upd = User.UserBuilder.aUser()
                .admin(false)
                .firstName("first-renamed")
                .lastName("last-renamed")
                .build();

        User ret = interactor.editUser(1, upd);
    }

    @Test
    public void testGetUser() throws Exception {
        User user = interactor.getUser(1);
    }

    @Test
    public void testGetUser1() throws Exception {
        User user = interactor.getUser("me@example.com");
        assertEquals("me@example.com", user.getEmail());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Setup
        User user = User.UserBuilder.aUser().email("me2@example.com").build();
        User ret = interactor.addUser(user);

        List<User> users = interactor.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("me@example.com", users.get(0).getEmail());
    }

    @Test(expected = RequirementNotMetException.class)
    public void testRemoveUser() throws Exception {
        interactor.removeUser(1);

        // This should now throw
        interactor.getUser(1);
    }

    @Test
    public void testGetTeams() throws Exception {
        UserDAOProjectMock daoMock = new UserDAOProjectMock();
        UserDAOFactory userDAOFactory = new UserDAOFactory();
        userDAOFactory.setInstance(daoMock);
        DAOProvider.setInstance("user", userDAOFactory);

        List<Team> teams = interactor.getTeams(1, false);
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }
}