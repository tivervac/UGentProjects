package be.ugent.vopro1.rest.mapper;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.util.LinkableList;
import org.aikodi.lang.funky.executors.Actor;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class HateoasWebMapperTest {

    private WebMapper mapper = WebMapperProvider.get(WebMapperProvider.HATEOAS_MAPPING);

    @Test
    public void testMapResult() throws Exception {
        Actor actor1 = new Actor("actor1");
        Actor actor2 = new Actor("actor2");

        ResponseEntity<?> ent = mapper.mapResult(new Result(new SuccessResult(), actor1), RequestMethod.GET, "");
        assertNotNull(ent);
        assertTrue(ent.getStatusCode().is2xxSuccessful());
        assertEquals(200, ent.getStatusCode().value());
        assertThat(ent.getBody(), instanceOf(LinkableList.class));

        ent = mapper.mapResult(new Result(new SuccessResult(), Arrays.asList(actor1, actor2)), RequestMethod.GET, "");
        assertNotNull(ent);
        assertTrue(ent.getStatusCode().is2xxSuccessful());
        assertEquals(200, ent.getStatusCode().value());
        assertThat(ent.getBody(), instanceOf(LinkableList.class));

        ent = mapper.mapResult(new Result(new SuccessResult(), actor1), RequestMethod.POST, "");
        assertNotNull(ent);
        assertTrue(ent.getStatusCode().is2xxSuccessful());
        assertEquals(201, ent.getStatusCode().value());
        assertThat(ent.getBody(), instanceOf(LinkableList.class));

        ent = mapper.mapResult(new Result(new SuccessResult(), actor1), RequestMethod.DELETE, "");
        assertNotNull(ent);
        assertTrue(ent.getStatusCode().is2xxSuccessful());
        assertEquals(204, ent.getStatusCode().value());

        ent = mapper.mapResult(new Result(new SuccessResult(), actor1), RequestMethod.PATCH, "");
        assertNotNull(ent);
        assertTrue(ent.getStatusCode().is2xxSuccessful());
        assertEquals(200, ent.getStatusCode().value());
        assertThat(ent.getBody(), instanceOf(LinkableList.class));

        //FIXME wat
        /*
        Arrays.asList(Result.ResultType.values()).forEach(rt -> {
            ResponseEntity<?> resp = mapper.mapResult(new Result(rt, actor1), RequestMethod.GET, "");
            if (rt.success()) {
                assertTrue(resp.getStatusCode().is2xxSuccessful());
            } else if (rt.name().contains("JSON") || rt.name().equalsIgnoreCase("DB_INVALID_QUERY")
                    || rt.name().equalsIgnoreCase("NO_PERMISSION") || rt.name().equalsIgnoreCase("WRONG_CREDENTIALS")) {
                assertTrue(resp.getStatusCode().is4xxClientError());
            } else {
                assertTrue(resp.getStatusCode().is5xxServerError());
            }
        });*/
    }
}