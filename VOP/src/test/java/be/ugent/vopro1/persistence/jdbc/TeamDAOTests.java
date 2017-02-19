package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.TeamDAO;
import org.junit.Ignore;
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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql
@Transactional
public class TeamDAOTests {

    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;
    private TeamDAO dao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        this.dao = context.getBean("teamDAO", TeamDAO.class);
    }

    @Test
    public void getTeam() {
        PersistentTeam team = dao.getById(1);
        assertNotNull(team);
        assertEquals(1, team.getId());
        assertEquals("team1", team.getName());

        team = dao.getByName("team2");
        assertNotNull(team);
        assertEquals(2, team.getId());
        assertEquals("team2", team.getName());
    }

    @Test
    public void existsTeam() {
        assertTrue(dao.exists(1));
        assertFalse(dao.exists(4));

        assertTrue(dao.exists("team2"));
        assertFalse(dao.exists("team3"));
    }

    @Test
    public void deleteTeam() {
        assertEquals(2, countRows("team"));
        dao.deleteById(1);
        assertEquals(1, countRows("team"));
        dao.deleteByName("team2");
        assertEquals(0, countRows("team"));
    }

    @Test
    public void createTeam() {
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam()
                .name("team3")
                .leader(1)
                .build();

        team = dao.save(team);
        assertNotNull(team);
        assertEquals("team3", team.getName());

        team = dao.getByName("team3");
        assertNotNull(team);
        assertEquals("team3", team.getName());
    }

    @Test
    public void updateTeam() {
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam()
                .name("team3")
                .leader(1)
                .build();

        team = dao.save(team);
        team.setName("team3-updated");
        dao.update(team);

        team = dao.getByName("team3-updated");
        assertNotNull(team);
        assertEquals("team3-updated", team.getName());
    }

    @Test
    //@Ignore("There is no TeamDAO#getAllTeams() method yet")
    public void getAllTeams() {
        List<PersistentTeam> teams = dao.getAll();
        assertNotNull(teams);
        assertEquals(2, teams.size());
    }

    @Test
    public void getAllMembers() {
        List<PersistentUser> members = dao.getAllMembersById(1);
        assertNotNull(members);
        assertEquals(2, members.size());
        assertEquals("me@example.com", members.get(0).getEmail());
        assertEquals("me2@example.com", members.get(1).getEmail());

        members = dao.getAllMembersByName("team2");
        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals("me@example.com", members.get(0).getEmail());
    }

    @Test
    public void memberManagement() {
        dao.addMember(1, 3);
        assertEquals(3, dao.getAllMembersById(1).size());
        dao.deleteMember(1, 1);
        assertEquals(2, dao.getAllMembersById(1).size());
    }

    @Test
    public void getAllProjects() {
        List<PersistentProject> projects = dao.getAllProjectsById(1);
        assertNotNull(projects);
        assertEquals(2, projects.size());

        projects = dao.getAllProjectsByName("team1");
        assertNotNull(projects);
        assertEquals(2, projects.size());
    }

    @Test
    public void projectManagement() {
        List<PersistentProject> projects = dao.getAllProjectsById(2);
        assertEquals(0, projects.size());

        dao.addProject(2, 1);

        projects = dao.getAllProjectsById(2);
        assertEquals(1, projects.size());

        dao.deleteProject(2, 1);

        projects = dao.getAllProjectsById(2);
        assertEquals(0, projects.size());
    }

    protected int countRows(String table) {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, table);
    }
}
