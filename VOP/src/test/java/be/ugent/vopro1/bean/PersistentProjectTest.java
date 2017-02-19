package be.ugent.vopro1.bean;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PersistentProjectTest {

    @Test
    public void testConstructor() throws Exception {
        PersistentProject proj = createProject();
        assertNotNull(proj);

        proj = createLoadedProject();
        assertNotNull(proj);
    }

    @Test
    public void testGetId() throws Exception {
        PersistentProject proj = createProject();
        assertEquals(-1, proj.getId());

        proj = createLoadedProject();
        assertEquals(5, proj.getId());
    }

    @Test
    public void testGetName() throws Exception {
        PersistentProject proj = createProject();
        assertEquals("project", proj.getName());
    }

    @Test
    public void testSetName() throws Exception {
        PersistentProject proj = createProject();
        proj.setName("project renamed");
        assertEquals("project renamed", proj.getName());
    }

    public PersistentProject createProject() {
        return new PersistentProject("project", 1);
    }

    public PersistentProject createLoadedProject() {
        return new PersistentProject(5, "project", 1);
    }
}