package be.ugent.vopro1.model;

import be.ugent.vopro1.util.Interval;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class AssignmentTest {

    private Assignment ass;
    private Interval interval;
    private UsecaseEntity entity;
    private AvailableUser availableUser;
    private Task task;

    @Before
    public void setUp() {
        User user = User.UserBuilder.aUser().email("blah@blah.com").build();
        availableUser = new AvailableUser(user, 10);
        interval = new Interval(LocalDateTime.parse("2015-05-17T02:00"),
                LocalDateTime.parse("2015-05-25T02:00"));
        entity = new UsecaseEntity();
        task = Task.TaskBuilder.aTask().priority(1).workload(10L).usecase(entity).build();
        this.ass = new Assignment(availableUser, interval, task);
    }

    @Test
    public void testGetInterval() throws Exception {
        assertEquals(interval, ass.getInterval());
    }

    @Test
    public void testSetInterval() throws Exception {
        Interval newInterval = new Interval(LocalDateTime.parse("2015-05-19T02:00"),
                LocalDateTime.parse("2015-05-20T02:00"));
        ass.setInterval(newInterval);
        assertEquals(newInterval, ass.getInterval());
    }

    @Test
    public void testGetStartDate() throws Exception {
        assertEquals(LocalDateTime.parse("2015-05-17T02:00"), ass.getStartDate());
    }

    @Test
    public void testSetStartDate() throws Exception {
        ass.setStartDate(LocalDateTime.parse("2015-05-19T02:00"));
        assertEquals(LocalDateTime.parse("2015-05-19T02:00"), ass.getStartDate());
    }

    @Test
    public void testGetEnd() throws Exception {
        assertEquals(LocalDateTime.parse("2015-05-25T02:00"), ass.getEnd());
    }

    @Test
    public void testSetEndDate() throws Exception {
        ass.setEndDate(LocalDateTime.parse("2015-05-20T02:00"));
        assertEquals(LocalDateTime.parse("2015-05-20T02:00"), ass.getEnd());
    }

    @Test
    public void testGetUser() throws Exception {
        assertEquals(availableUser, ass.getUser());
    }

    @Test
    public void testSetUser() throws Exception {
        User user = User.UserBuilder.aUser().email("blah2@blah.com").build();
        AvailableUser otherAvailableUser = new AvailableUser(user, 20);
        ass.setUser(otherAvailableUser);
        assertEquals(otherAvailableUser, ass.getUser());
    }

    @Test
    public void testGetTask() throws Exception {
        assertEquals(task, ass.getTask());
    }

    @Test
    public void testSetTask() throws Exception {
        Task otherTask = Task.TaskBuilder.aTask().priority(30).workload(5).usecase(new UsecaseEntity()).build();
        ass.setTask(otherTask);
        assertEquals(otherTask, ass.getTask());
    }

}