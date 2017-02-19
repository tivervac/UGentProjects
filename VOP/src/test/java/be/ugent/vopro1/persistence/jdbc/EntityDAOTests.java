package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.ProjectDAOFactory;
import org.junit.AfterClass;
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

import static junit.framework.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql
@Transactional
public class EntityDAOTests {

    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;
    private EntityDAO dao;

    @Autowired public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        this.dao = context.getBean("entityDAO", EntityDAO.class);

        // Make sure we use the correct Project DAO, otherwise no projects will be available
        ProjectDAOFactory mockProjectDAO = new ProjectDAOFactory();
        mockProjectDAO.setInstance(context.getBean("projectDAO", ProjectDAO.class));
        DAOProvider.setInstance("project", mockProjectDAO);
    }

    @AfterClass
    public static void tearDown() {
        DAOProvider.setInstance("project", new ProjectDAOFactory());
    }

    @Test
    public void getEntity() {
        PersistentObject en1 = dao.getById(1);
        assertEquals("doc1", en1.getName());
        assertEquals(1, en1.getProjectId());
        assertEquals("actor", en1.getType());
        assertEquals("{type: \"actor\", name: \"sysadmin\"}", en1.getBlob());

        PersistentObject en2 = dao.getByName("doc2", 2);
        assertEquals(2, en2.getId());
        assertEquals(2, en2.getProjectId());
        assertEquals("usecase", en2.getType());
        assertEquals("{type: \"usecase\", name: \"myusecase\"}", en2.getBlob());

        PersistentObject en3 = dao.getByName("doc2", "project2");
        assertEquals(2, en3.getId());
        assertEquals(2, en3.getProjectId());
        assertEquals("usecase", en3.getType());
        assertEquals("{type: \"usecase\", name: \"myusecase\"}", en3.getBlob());
    }

    @Test
    public void deleteEntity() {
        dao.deleteById(1);
        assertEquals(2, countRows("document"));

        dao.deleteByName("doc2", 2);
        assertEquals(1, countRows("document"));

        dao.deleteByName("doc3", "project2");
        assertEquals(0, countRows("document"));
    }

    @Test
    public void addEntity() {
        PersistentObject obj = new PersistentObject(2,
                                                    "test-doc4",
                                                    "concept",
                                                    "{type: \"concept\", name: \"myconcept\", attributes: []}");
        obj = dao.save(obj);

        assertEquals(4, countRows("document"));
        assertEquals(4, obj.getId());
    }

    @Test
    public void getAllEntities() {
        assertEquals(3, dao.getAll().size());
    }

    @Test
    public void existsEntity() {
        assertTrue(dao.exists(1));
        assertFalse(dao.exists(4));

        assertTrue(dao.exists("doc1", 1));
        assertFalse(dao.exists("doc1", 2));

        assertTrue(dao.exists("doc1", "project1"));
        assertTrue(dao.exists("doc2", "project2"));
    }

    @Test
    public void updateEntity() {
        PersistentObject obj = new PersistentObject(2,
                                                    "test-doc4",
                                                    "concept",
                                                    "{type: \"concept\", name: \"myconcept\", attributes: []}");
        obj = dao.save(obj);

        obj.setName("test-doc4-renamed");
        dao.update(obj);
        assertNotNull(dao.getByName("test-doc4-renamed", 2));

        obj.setBlob("{type: \"concept\", name: \"myconcept\", attributes: ['blah']}");
        dao.update(obj);
        assertEquals("{type: \"concept\", name: \"myconcept\", attributes: ['blah']}", dao.getByName("test-doc4-renamed", 2).getBlob());
    }

    protected int countRows(String table) {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, "document");
    }
}
