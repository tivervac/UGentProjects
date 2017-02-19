package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TeamMemberTableTest {

    private TeamMemberTable table;

    @Before
    public void initialize() {
        table = new TeamMemberTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("team_member", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(TeamMemberTable.TeamMemberColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(TeamMemberTable.TeamMemberColumn.values()).filter(TeamMemberTable.TeamMemberColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(TeamMemberTable.TeamMemberColumn.values()).filter(TeamMemberTable.TeamMemberColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(TeamMemberTable.TeamMemberColumn.TEAM_ID);
        table.check(TeamMemberTable.TeamMemberColumn.TEAM_ID, TeamMemberTable.TeamMemberColumn.USER_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

}