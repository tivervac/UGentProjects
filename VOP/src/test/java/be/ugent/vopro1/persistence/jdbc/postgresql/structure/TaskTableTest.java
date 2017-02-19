package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TaskTableTest {
    
    private TaskTable table;

    @Before
    public void setUp() throws Exception {
        table = new TaskTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("task", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(TaskTable.TaskColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(TaskTable.TaskColumn.values())
                .filter(TaskTable.TaskColumn::isSelect).toArray(), table.selects());

    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(TaskTable.TaskColumn.values())
                .filter(TaskTable.TaskColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(TaskTable.TaskColumn.USE_CASE_ID);
        table.check(TaskTable.TaskColumn.PRIORITY, TaskTable.TaskColumn.WORKLOAD);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(EntityTable.EntityColumn.PROJECT);
    }
}