package be.ugent.vopro1.bean;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PersistentObjectTest {

    @Test
    public void testConstructor() throws Exception {
        PersistentObject obj = createObject();
        assertNotNull(obj);

        obj = createLoadedObject();
        assertNotNull(obj);
        assertEquals(5, obj.getId());
    }

    @Test
    public void testGetId() throws Exception {
        PersistentObject obj = createObject();
        assertEquals(-1, obj.getId());

        obj = createLoadedObject();
        assertEquals(5, obj.getId());
    }

    @Test
    public void testGetProjectId() throws Exception {
        PersistentObject obj = createObject();
        assertEquals(2, obj.getProjectId());
    }

    @Test
    public void testGetName() throws Exception {
        PersistentObject obj = createObject();
        assertEquals("object", obj.getName());
    }

    @Test
    public void testSetName() throws Exception {
        PersistentObject obj = createObject();
        obj.setName("object renamed");
        assertEquals("object renamed", obj.getName());
    }

    @Test
    public void testGetType() throws Exception {
        PersistentObject obj = createObject();
        assertEquals("actor", obj.getType());
    }

    @Test
    public void testSetType() throws Exception {
        PersistentObject obj = createObject();
        obj.setType("concept");
        assertEquals("concept", obj.getType());
    }

    @Test
    public void testGetBlob() throws Exception {
        PersistentObject obj = createObject();
        assertEquals("{type: 'actor'}", obj.getBlob());
    }

    @Test
    public void testSetBlob() throws Exception {
        PersistentObject obj = createObject();
        obj.setBlob("{type: 'usecase'}");
        assertEquals("{type: 'usecase'}", obj.getBlob());
    }

    public PersistentObject createObject() {
        return new PersistentObject(2, "object", "actor", "{type: 'actor'}");
    }

    public PersistentObject createLoadedObject() {
        return new PersistentObject(5, 2, "object", "actor", "{type: 'actor'}");
    }
}