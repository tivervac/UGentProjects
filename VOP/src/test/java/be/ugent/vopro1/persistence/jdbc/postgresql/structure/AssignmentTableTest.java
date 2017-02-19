package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AssignmentTableTest {

    private AssignmentTable table;

    @Before
    public void setUp() throws Exception {
        table = new AssignmentTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("assignment", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(AssignmentTable.AssignmentColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(AssignmentTable.AssignmentColumn.values())
                .filter(AssignmentTable.AssignmentColumn::isSelect).toArray(), table.selects());

    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(AssignmentTable.AssignmentColumn.values())
                .filter(AssignmentTable.AssignmentColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(AssignmentTable.AssignmentColumn.END_DATE);
        table.check(AssignmentTable.AssignmentColumn.PROJECT_ID, AssignmentTable.AssignmentColumn.START_DATE,
                AssignmentTable.AssignmentColumn.END_DATE, AssignmentTable.AssignmentColumn.TASK_ID,
                AssignmentTable.AssignmentColumn.USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(EntityTable.EntityColumn.PROJECT);
    }
}