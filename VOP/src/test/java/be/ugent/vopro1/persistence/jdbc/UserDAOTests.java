package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql
@Transactional
public class UserDAOTests {

    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;
    private UserDAO dao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        this.dao = context.getBean("userDAO", UserDAO.class);
    }

    @Test
    public void getUser() {
        PersistentUser user = dao.getById(1);
        assertNotNull(user);
        assertEquals(user.getFirstName(), "first");
        assertEquals(user.getLastName(), "last");
        assertEquals(user.getEmail(), "me@example.com");
        assertEquals(user.isAdmin(), true);
        assertEquals(user.getId(), 1);

        user = dao.getByEmail("me2@example.com");
        assertEquals(user.getFirstName(), "first2");
        assertEquals(user.getLastName(), "last2");
        assertEquals(user.getEmail(), "me2@example.com");
        assertEquals(user.isAdmin(), false);
        assertEquals(user.getId(), 2);
    }

    @Test
    public void deleteUser() {
        assertEquals(2, countRows("person"));
        dao.deleteById(1);
        assertEquals(1, countRows("person"));
        dao.deleteByEmail("me2@example.com");
        assertEquals(0, countRows("person"));
    }

    @Test
    public void createUser() {
        assertEquals(2, countRows("person"));
        PersistentUser.PersistentUserBuilder builder = PersistentUser.PersistentUserBuilder.aPersistentUser()
                .admin(false)
                .hashedPassword("so-hashed")
                .firstName("test")
                .lastName("mctester")
                .but();

        PersistentUser user1 = builder.email("test@tester.net").build();
        PersistentUser user2 = builder.email("test2@tester.net").build();
        System.out.println(user1);
        System.out.println(user2);
        user1 = dao.save(user1);
        assertNotNull(user1);
        assertEquals(3, user1.getId());
        assertEquals("test@tester.net", user1.getEmail());
        assertEquals(3, countRows("person"));

        user2 = dao.save(user2);
        assertNotNull(2);
        assertEquals(4, user2.getId());
        assertEquals("test2@tester.net", user2.getEmail());
        assertEquals(4, countRows("person"));
    }

    @Test
    public void getAllUsers() {
        List<PersistentUser> list = dao.getAll();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("me@example.com", list.get(0).getEmail());
        assertEquals("me2@example.com", list.get(1).getEmail());
    }

    @Test
    public void updateUser() {
        PersistentUser user = PersistentUser.PersistentUserBuilder.aPersistentUser()
                .admin(false)
                .hashedPassword("so-hashed")
                .firstName("test")
                .lastName("mctester")
                .email("test@tester.net").build();

        user = dao.save(user);

        // These values CAN be updated
        user.setAdmin(true);
        user.setFirstName("test-updated");
        user.setLastName("mctester-updated");
        dao.update(user);

        PersistentUser userUpdated = dao.getByEmail("test@tester.net");
        assertEquals(true, userUpdated.isAdmin());
        assertEquals("test-updated", userUpdated.getFirstName());
        assertEquals("mctester-updated", userUpdated.getLastName());

        user = PersistentUser.PersistentUserBuilder.aPersistentUser()
                .admin(false)
                .hashedPassword("so-hashed")
                .firstName("test")
                .lastName("mctester")
                .email("test2@tester.net").build();

        user = dao.save(user);

        // These values CANNOT be updated
        user.setHashedPassword("so-hashed-updated");
        dao.update(user);

        userUpdated = dao.getByEmail("test2@tester.net");
        assertNotNull(userUpdated);
        assertEquals("test2@tester.net", userUpdated.getEmail());
        assertEquals(4, userUpdated.getId());
        assertEquals("so-hashed", userUpdated.getPassword());
    }

    @Test
    public void testTeams() {
        List<PersistentTeam> teams = dao.getAllTeamsById(1);
        assertEquals(2, teams.size());

        teams = dao.getAllTeamsByEmail("me2@example.com");
        assertEquals(1, teams.size());
    }

    protected int countRows(String table) {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, table);
    }
}
