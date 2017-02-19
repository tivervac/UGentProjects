package be.ugent.vopro1.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {

    private Task task;
    private UsecaseEntity entity;

    @Before
    public void setUp() throws Exception {
        this.entity = new UsecaseEntity("lolpls");
        this.task = Task.TaskBuilder.aTask().workload(10).priority(5).usecase(entity).build();
    }

    @Test
    public void testGetUsecase() throws Exception {
        assertEquals(entity, task.getUsecase());
    }

    @Test
    public void testSetUsecase() throws Exception {
        UsecaseEntity otherEntity = new UsecaseEntity("somethingelse");
        task.setUsecase(otherEntity);
        assertEquals(otherEntity, task.getUsecase());
    }

    @Test
    public void testGetPriority() throws Exception {
        assertEquals(5, task.getPriority());
    }

    @Test
    public void testSetPriority() throws Exception {
        task.setPriority(6);
        assertEquals(6, task.getPriority());
    }

    @Test
    public void testGetWorkload() throws Exception {
        assertEquals(10, task.getWorkload());
    }

    @Test
    public void testSetWorkload() throws Exception {
        task.setWorkload(11);
        assertEquals(11, task.getWorkload());
    }
}