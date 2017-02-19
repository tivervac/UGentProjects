package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ProjectAnalystTableTest {

    private ProjectAnalystTable table;

    @Before
    public void initialize() {
        table = new ProjectAnalystTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("project_analyst", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(ProjectAnalystTable.ProjectAnalystColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(ProjectAnalystTable.ProjectAnalystColumn.values()).filter(ProjectAnalystTable.ProjectAnalystColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(ProjectAnalystTable.ProjectAnalystColumn.values()).filter(ProjectAnalystTable.ProjectAnalystColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(ProjectAnalystTable.ProjectAnalystColumn.PROJECT_ID);
        table.check(ProjectAnalystTable.ProjectAnalystColumn.PROJECT_ID, ProjectAnalystTable.ProjectAnalystColumn.USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

}