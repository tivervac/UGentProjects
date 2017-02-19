package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.ProjectDAO;
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql
@Transactional
public class ProjectDAOTests {

    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;
    private ProjectDAO dao;

    @Autowired public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        this.dao = context.getBean("projectDAO", ProjectDAO.class);
    }

    @Test
    public void getProject() {
        PersistentProject proj = dao.getById(1);
        assertEquals("project1", proj.getName());

        proj = dao.getByName("project2");
        assertEquals(2, proj.getId());
    }

    @Test
    public void deleteProject() {
        dao.deleteById(1);
        assertEquals(1, countRows("project"));

        dao.deleteByName("project2");
        assertEquals(0, countRows("project"));
    }

    @Test
    public void createProject() {
        PersistentProject proj = new PersistentProject("test-project1", 1);
        dao.save(proj);

        assertEquals(3, countRows("project"));
    }

    @Test
    public void getAllProjects() {
        assertEquals(2, dao.getAll().size());

        assertEquals(2, dao.find("").size());
    }

    @Test
    public void updateProject() {
        PersistentProject proj = new PersistentProject("test-project1", 1);
        proj = dao.save(proj);

        proj.setName("test-project1-renamed");
        dao.update(proj);

        assertNotNull(dao.getByName("test-project1-renamed"));
    }

    @Test
    public void existsProject() {
        assertTrue(dao.exists(1));
        assertFalse(dao.exists(4));

        assertTrue(dao.exists("project2"));
        assertFalse(dao.exists("project3"));
    }

    @Test
    public void getTeams() {
        List<PersistentTeam> teams = dao.getAllTeamsById(1);

        assertNotNull(teams);
        assertEquals(2, teams.size());

        teams = dao.getAllTeamsByName("project2");

        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void getAnalysts() {
        List<PersistentUser> analysts = dao.getAllAnalystsById(1);
        assertNotNull(analysts);
        assertEquals(2, analysts.size());

        analysts = dao.getAllAnalystsByName("project1");
        assertNotNull(analysts);
        assertEquals(2, analysts.size());
    }

    //TODO: Test workload
    @Test
    public void analystManagement() {
        List<PersistentUser> analysts = dao.getAllAnalystsById(2);
        assertEquals(0, analysts.size());

        dao.addAnalyst(2, 2, 10);

        analysts = dao.getAllAnalystsById(2);
        assertEquals(1, analysts.size());

        dao.deleteAnalyst(2, 2);

        analysts = dao.getAllAnalystsById(2);
        assertEquals(0, analysts.size());
    }

    protected int countRows(String table) {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, "project");
    }
}
