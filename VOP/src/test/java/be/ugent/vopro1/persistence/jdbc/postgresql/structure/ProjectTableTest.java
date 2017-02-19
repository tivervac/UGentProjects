package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ProjectTableTest {

    private ProjectTable table;

    @Before
    public void initialize() {
        table = new ProjectTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("project", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(ProjectTable.ProjectColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(ProjectTable.ProjectColumn.values()).filter(ProjectTable.ProjectColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(ProjectTable.ProjectColumn.values()).filter(ProjectTable.ProjectColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(ProjectTable.ProjectColumn.ID);
        table.check(ProjectTable.ProjectColumn.ID, ProjectTable.ProjectColumn.NAME);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

}