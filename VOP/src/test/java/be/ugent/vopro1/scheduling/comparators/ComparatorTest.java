package be.ugent.vopro1.scheduling.comparators;

import be.ugent.vopro1.model.*;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComparatorTest {

    @Test
    public void testCompareAnalyst() throws Exception {
        AnalystComparator comparator = new AnalystComparator();
        User user = User.UserBuilder.aUser().email("test@test.com").build();
        AvailableUser user1 = new AvailableUser(user, 10);
        AvailableUser user2 = new AvailableUser(user, 10);
        AvailableUser user3 = new AvailableUser(user, 5);
        AvailableUser user4 = new AvailableUser(user, 15);

        assertTrue(comparator.compare(user1, user1) == 0);
        assertTrue(comparator.compare(user1, user2) == 0);
        assertTrue(comparator.compare(user1, user3) > 0);
        assertTrue(comparator.compare(user1, user4) < 0);
    }

    @Test
    public void testCompareProcess() throws Exception {
        ProcessComparator comparator = new ProcessComparator();
        ProcessEntity process1 = ProcessEntity.ProcessEntityBuilder.aProcessEntity()
                .priority(10)
                .name("blah")
                .useCases(Lists.newArrayList())
                .build();
        ProcessEntity process2 = ProcessEntity.ProcessEntityBuilder.aProcessEntity()
                .priority(10)
                .name("blah2")
                .useCases(Lists.newArrayList())
                .build();
        ProcessEntity process3 = ProcessEntity.ProcessEntityBuilder.aProcessEntity()
                .priority(5)
                .name("blah3")
                .useCases(Lists.newArrayList())
                .build();
        ProcessEntity process4 = ProcessEntity.ProcessEntityBuilder.aProcessEntity()
                .priority(15)
                .name("blah4")
                .useCases(Lists.newArrayList())
                .build();

        assertTrue(comparator.compare(process1, process1) == 0);
        assertTrue(comparator.compare(process1, process2) == 0);

        // FIXME This might be wrong in the code or documentation
        assertTrue(comparator.compare(process1, process3) > 0);      // Other process has lower priority
        assertTrue(comparator.compare(process1, process4) < 0);      // Other process has higher priority
    }

    @Test
    public void testCompareTask() {
        TaskComparator comparator = new TaskComparator();
        UsecaseEntity useCase = new UsecaseEntity("blah");

        Task task1 = Task.TaskBuilder.aTask().priority(2).workload(10).usecase(useCase).build();
        Task task2 = Task.TaskBuilder.aTask().priority(2).workload(10).usecase(useCase).build();
        Task task3 = Task.TaskBuilder.aTask().priority(2).workload(5).usecase(useCase).build();
        Task task4 = Task.TaskBuilder.aTask().priority(2).workload(15).usecase(useCase).build();
        Task task5 = Task.TaskBuilder.aTask().priority(3).workload(10).usecase(useCase).build();
        Task task6 = Task.TaskBuilder.aTask().priority(3).workload(5).usecase(useCase).build();
        Task task7 = Task.TaskBuilder.aTask().priority(3).workload(15).usecase(useCase).build();
        Task task8 = Task.TaskBuilder.aTask().priority(1).workload(10).usecase(useCase).build();
        Task task9 = Task.TaskBuilder.aTask().priority(1).workload(5).usecase(useCase).build();
        Task task10 = Task.TaskBuilder.aTask().priority(1).workload(15).usecase(useCase).build();

        assertTrue(comparator.compare(task1, task2) == 0);      // Equal priority and workload
        // FIXME the following might be wrong in the documentation or the code
        assertTrue(comparator.compare(task1, task3) > 0);       // Other has lower workload
        assertTrue(comparator.compare(task1, task4) < 0);       // Other has higher workload
        assertTrue(comparator.compare(task1, task5) < 0);       // Other has higher priority
        assertTrue(comparator.compare(task1, task6) < 0);
        assertTrue(comparator.compare(task1, task7) < 0);
        assertTrue(comparator.compare(task1, task8) > 0);       // Other has lower priority
        assertTrue(comparator.compare(task1, task9) > 0);
        assertTrue(comparator.compare(task1, task10) > 0);

    }
}