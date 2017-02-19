package be.ugent.vopro1.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntityProjectTest {

    @Test
    public void testConstructor() throws Exception {
        EntityProject proj = createEntityProject();
        assertNotNull(proj);

        proj = createEmptyEntityProject();
        assertNotNull(proj);
        assertEquals("", proj.getName());
    }

    @Test
    public void testGetName() throws Exception {
        EntityProject proj = createEntityProject();
        assertEquals("project", proj.getName());
    }


    private static EntityProject createEntityProject() {
        return new EntityProject("project");
    }

    private static EntityProject createEmptyEntityProject() {
        return new EntityProject("");
    }
}