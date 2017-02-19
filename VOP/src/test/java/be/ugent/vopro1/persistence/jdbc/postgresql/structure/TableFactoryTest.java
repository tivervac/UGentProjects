package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Table;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableFactoryTest {

    private static final String[] reprs = {
            "entity", "project", "user", "team", "member", "analyst", "teamProject"
    };

    private static final Table[] tables = {
            new EntityTable(), new ProjectTable(), new UserTable(), new TeamTable(), new TeamMemberTable(),
            new ProjectAnalystTable(), new TeamProjectTable()
    };

    @Test
    public void testGetInstance() {
        for (int i = 0; i < reprs.length; i++) {
            assertThat(TableFactory.getInstance(reprs[i]), instanceOf(tables[i].getClass()));
        }
    }
}
