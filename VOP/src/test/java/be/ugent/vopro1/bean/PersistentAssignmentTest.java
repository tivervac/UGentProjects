package be.ugent.vopro1.bean;

import be.ugent.vopro1.util.Interval;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class PersistentAssignmentTest {

    private PersistentAssignment assignment;
    private Interval interval;

    @Before
    public void setUp() {
        interval = new Interval(LocalDateTime.MIN, LocalDateTime.MAX);
        assignment = PersistentAssignment.PersistentAssignmentBuilder.aPersistentAssignment()
                .interval(interval)
                .taskId(2)
                .userId(1)
                .build();
    }

    @Test
    public void testGetUserId() throws Exception {
        assertEquals(1, assignment.getUserId());
    }

    @Test
    public void testGetInterval() throws Exception {
        assertEquals(interval, assignment.getInterval());
    }

    @Test
    public void testGetTaskId() throws Exception {
        assertEquals(2, assignment.getTaskId());
    }

    @Test
    public void testUserId() throws Exception {
        PersistentAssignment ass2 = assignment.userId(3);
        assertEquals(3, ass2.getUserId());
        assertEquals(2, ass2.getTaskId());
        assertEquals(interval, ass2.getInterval());
    }

    @Test
    public void testInterval() throws Exception {
        Interval int2 = new Interval(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        PersistentAssignment ass2 = assignment.interval(int2);
        assertEquals(1, ass2.getUserId());
        assertEquals(2, ass2.getTaskId());
        assertEquals(int2, ass2.getInterval());

    }

    @Test
    public void testTaskId() throws Exception {
        PersistentAssignment ass2 = assignment.taskId(3);
        assertEquals(1, ass2.getUserId());
        assertEquals(3, ass2.getTaskId());
        assertEquals(interval, ass2.getInterval());
    }
}