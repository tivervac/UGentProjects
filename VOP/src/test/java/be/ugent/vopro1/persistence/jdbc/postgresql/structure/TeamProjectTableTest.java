package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TeamProjectTableTest {

    private TeamProjectTable table;

    @Before
    public void initialize() {
        table = new TeamProjectTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("team_project", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(TeamProjectTable.TeamProjectColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(TeamProjectTable.TeamProjectColumn.values()).filter(TeamProjectTable.TeamProjectColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(TeamProjectTable.TeamProjectColumn.values()).filter(TeamProjectTable.TeamProjectColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(TeamProjectTable.TeamProjectColumn.TEAM_ID);
        table.check(TeamProjectTable.TeamProjectColumn.TEAM_ID, TeamProjectTable.TeamProjectColumn.PROJECT_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

}