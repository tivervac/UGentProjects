package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TeamTableTest {

    private TeamTable table;

    @Before
    public void initialize() {
        table = new TeamTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("team", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(TeamTable.TeamColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(TeamTable.TeamColumn.values()).filter(TeamTable.TeamColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(TeamTable.TeamColumn.values()).filter(TeamTable.TeamColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(TeamTable.TeamColumn.ID);
        table.check(TeamTable.TeamColumn.ID, TeamTable.TeamColumn.NAME);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

}