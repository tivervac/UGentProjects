package be.ugent.vopro1.bean;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersistentTaskTest {

    private PersistentTask task;

    @Before
    public void setUp() throws Exception {
        task = PersistentTask.PersistentTaskBuilder.aPersistentTask()
                .useCaseId(1)
                .priority(10)
                .workload(1000)
                .build();
    }

    @Test
    public void testGetWorkload() throws Exception {
        assertEquals(1000, task.getWorkload());
    }

    @Test
    public void testGetPriority() throws Exception {
        assertEquals(10, task.getPriority());
    }

    @Test
    public void testGetUseCaseId() throws Exception {
        assertEquals(1, task.getUseCaseId());
    }

    @Test
    public void testWorkload() throws Exception {
        PersistentTask task2 = task.workload(2000);
        assertEquals(1, task2.getUseCaseId());
        assertEquals(10, task2.getPriority());
        assertEquals(2000, task2.getWorkload());
    }

    @Test
    public void testPriority() throws Exception {
        PersistentTask task2 = task.priority(20);
        assertEquals(1, task2.getUseCaseId());
        assertEquals(20, task2.getPriority());
        assertEquals(1000, task2.getWorkload());
    }

    @Test
    public void testUseCaseId() throws Exception {
        PersistentTask task2 = task.useCaseId(2);
        assertEquals(2, task2.getUseCaseId());
        assertEquals(10, task2.getPriority());
        assertEquals(1000, task2.getWorkload());
    }
}