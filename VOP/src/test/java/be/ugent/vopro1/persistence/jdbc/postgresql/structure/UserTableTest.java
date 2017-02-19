package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserTableTest {

    private UserTable table;

    @Before
    public void initialize() {
        table = new UserTable();
    }

    @Test
    public void testRepr() throws Exception {
        assertEquals("person", table.repr());
    }

    @Test
    public void testColumns() throws Exception {
        assertArrayEquals(UserTable.UserColumn.values(), table.columns());
    }

    @Test
    public void testSelects() throws Exception {
        assertArrayEquals(Arrays.stream(UserTable.UserColumn.values()).filter(UserTable.UserColumn::isSelect).toArray(), table.selects());
    }

    @Test
    public void testInserts() throws Exception {
        assertArrayEquals(Arrays.stream(UserTable.UserColumn.values()).filter(UserTable.UserColumn::isInsert).toArray(), table.inserts());
    }

    @Test
    public void testCheck() throws Exception {
        table.check(UserTable.UserColumn.ID);
        table.check(UserTable.UserColumn.ID, UserTable.UserColumn.FIRST_NAME);
    }

    @Test(expected = RuntimeException.class)
    public void testCheck1() throws Exception {
        table.check(EntityTable.EntityColumn.ID);
        table.check(EntityTable.EntityColumn.ID, EntityTable.EntityColumn.NAME);
    }

}