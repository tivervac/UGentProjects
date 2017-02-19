package be.ugent.vopro1.model;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProcessEntityTest {

    private ProcessEntity process;

    @Before
    public void setUp() throws Exception {
        process = new ProcessEntity("lol");
        process.setPriority(5);
    }

    @Test
    public void testGetPriority() throws Exception {
        assertEquals(5, process.getPriority());
    }

    @Test
    public void testSetPriority() throws Exception {
        process.setPriority(10);
        assertEquals(10, process.getPriority());
    }

    @Test
    public void testSetUseCases() throws Exception {
        List<String> useCases = Lists.newArrayList("haha", "it", "is", "true");
        process.setUseCases(useCases);
        assertEquals(4, process.getUseCases().size());
        assertEquals("haha", process.getUseCases().get(0));
        assertEquals("true", process.getUseCases().get(3));
    }

    @Test
    public void testBuilder() {
        ProcessEntity entity = ProcessEntity.ProcessEntityBuilder.aProcessEntity()
                .name("entity")
                .priority(20)
                .useCases(Lists.newArrayList("haha", "lol"))
                .build();
        assertEquals("entity", entity.name());
        assertEquals(20, entity.getPriority());
        assertEquals(2, entity.getUseCases().size());
        assertEquals("haha", entity.getUseCases().get(0));
        assertEquals("lol", entity.getUseCases().get(1));
    }
}