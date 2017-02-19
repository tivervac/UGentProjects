package be.ugent.vopro1.controllers;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.rest.controller.ActorRestController;
import be.ugent.vopro1.rest.controller.ProjectRestController;
import org.aikodi.chameleon.workspace.ProjectException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.util.Base64;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
@WebAppConfiguration
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_document.sql", "create_actors.sql"})
public class TestActor {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	@Autowired
	private DataSource dataSource;

	private String basicHeader;
	private MockMvc mockMvcProjectController;
	private MockMvc mockMvcActorController;
	
	@Before
	public void setup() throws ProjectException {
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvcProjectController = MockMvcBuilders.standaloneSetup(new ProjectRestController()).build();
		mockMvcActorController = MockMvcBuilders.standaloneSetup(new ActorRestController()).build();
		Base64.Encoder encoder = Base64.getEncoder();
		basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
		CustomWorkspace workspace = WorkspaceFactory.getInstance();
		workspace.createProject("testproject");
	}
	
	/**
	 * Tests GET /actor
	 */
	@Test
	public void testGetAllActors() {
		try {
			mockMvcActorController
			        .perform(get("/actor")
					.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
     * Tests GET /project/{projectName}/actor
     */
    @Test
    public void testGetAllActorsInProject() {
        try {
            mockMvcActorController
                    .perform(get("/project/{projectName}/actor","testproject")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk());            
        } catch (Exception e) {
            fail();
        }
    }
    
    /**
     * Tests GET /project/{projectName}/actor/{actorName}
     */
    @Test
    public void testGetActor() {
        try {
            mockMvcActorController
                    .perform(get("/project/{projectName}/actor/{actorName}","testproject","actor1")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Tests POST /project/{projectName}/actor
     */
	@Test
	public void testCreateActor() {
		try {
			mockMvcActorController
			        .perform(post("/project/{projectName}/actor", "testproject")
							.content("{\"name\": \"actor4\"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isCreated());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Tests DELETE /project/{projectName}/actor/{actorName}
	 */
	@Test
	public void testRemoveActor() {
		try {
			mockMvcActorController
			        .perform(delete("/project/{projectName}/actor/{actorName}","testproject","actor2")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isNoContent());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
     * Tests PATCH /project/{projectName}/actor/{actorName}
     */
	@Test
	public void testUpdateActor() {
		try {
			mockMvcActorController
			        .perform(patch("/project/{projectName}/actor/{actorName}","testproject","actor3")
							.content("{\"name\": \"actor3patch\"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			                .andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}
}
