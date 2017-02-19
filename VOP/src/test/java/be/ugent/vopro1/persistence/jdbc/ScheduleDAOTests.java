package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.util.Interval;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql
@Transactional
public class ScheduleDAOTests {

    private JdbcTemplate jdbcTemplate;
    private ApplicationContext context;
    private ScheduleDAO dao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
        this.dao = context.getBean("scheduleDAO", ScheduleDAO.class);
    }

    @Test
    public void testSaveTaskForUseCase() throws Exception {
        PersistentTask task = dao.saveTaskForUseCase(
                PersistentTask.PersistentTaskBuilder.aPersistentTask()
                .useCaseId(3)
                .workload(300)
                .priority(30)
                .build()
        );
        assertNotNull(task);
    }

    @Test
    public void testGetTaskForUseCase() throws Exception {
        PersistentTask task = dao.getTaskForUseCase(1);
        assertNotNull(task);
        assertEquals(1, task.getUseCaseId());
        assertEquals(10, task.getPriority());
        assertEquals(100, task.getWorkload());
    }

    @Test
    public void testUpdateTaskForUseCase() throws Exception {
        PersistentTask task = dao.getTaskForUseCase(2);
        assertNotNull(task);
        assertEquals(2, task.getUseCaseId());
        assertEquals(20, task.getPriority());
        assertEquals(200, task.getWorkload());

        dao.updateTaskForUseCase(
                PersistentTask.PersistentTaskBuilder.aPersistentTask()
                        .useCaseId(2)
                        .priority(25)
                        .workload(250)
                        .build()
        );

        task = dao.getTaskForUseCase(2);
        assertNotNull(task);
        assertEquals(2, task.getUseCaseId());
        assertEquals(25, task.getPriority());
        assertEquals(250, task.getWorkload());
    }

    @Test
    public void testExistsTaskForUseCase() throws Exception {
        assertTrue(dao.existsTaskForUseCase(1));
        assertTrue(dao.existsTaskForUseCase(2));
        assertFalse(dao.existsTaskForUseCase(3));
    }

    @Test
    public void testDeleteTaskForUseCase() throws Exception {
        assertTrue(dao.existsTaskForUseCase(2));
        dao.deleteTaskForUseCase(2);
        assertFalse(dao.existsTaskForUseCase(2));
    }

    @Test
    public void testGetAssignmentsForProject() throws Exception {
        List<PersistentAssignment> asses = dao.getAssignmentsForProject(1);
        assertEquals(2, asses.size());
        PersistentAssignment ass1 = asses.get(0);
        assertEquals(1, ass1.getTaskId());
        assertEquals(1, ass1.getUserId());
        assertNotNull(ass1.getInterval());
    }

    @Test
    public void testSaveAssignmentsForProject() throws Exception {
        List<PersistentAssignment> asses = Lists.newArrayList(
                PersistentAssignment.PersistentAssignmentBuilder.aPersistentAssignment()
                        .taskId(2)
                        .userId(2)
                        .interval(new Interval(LocalDateTime.parse("2015-05-14T02:00"),
                                LocalDateTime.parse("2015-05-15T02:00")))
                        .build()
        );
        dao.saveAssignmentsForProject(2, asses);
        asses = dao.getAssignmentsForProject(2);
        assertEquals(1, asses.size());
        PersistentAssignment ass1 = asses.get(0);
        assertEquals(2, ass1.getTaskId());
        assertEquals(2, ass1.getUserId());
        assertEquals(new Interval(LocalDateTime.parse("2015-05-14T02:00"),
                LocalDateTime.parse("2015-05-15T02:00")), ass1.getInterval());
    }

    @Test
    public void testRemoveSchedule() throws Exception {
        List<PersistentAssignment> asses = dao.getAssignmentsForProject(1);
        assertEquals(2, asses.size());
        dao.removeSchedule(1);
        asses = dao.getAssignmentsForProject(1);
        assertEquals(0, asses.size());
    }

}
