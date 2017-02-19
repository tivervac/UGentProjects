package be.ugent.vopro1.controllers;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.rest.controller.ConceptRestController;
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

import java.util.Base64;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
@WebAppConfiguration
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_document.sql", "create_concepts.sql"})
public class TestConcept {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	String basicHeader;
	private MockMvc mockMvcProjectController;
	private MockMvc mockMvcConceptController;
	
	@Before
	public void setup() throws ProjectException {
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvcProjectController = MockMvcBuilders.standaloneSetup(new ProjectRestController()).build();
		mockMvcConceptController = MockMvcBuilders.standaloneSetup(new ConceptRestController()).build();
		Base64.Encoder encoder = Base64.getEncoder();
		basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
		CustomWorkspace workspace = WorkspaceFactory.getInstance();
		workspace.createProject("testproject");
	}

	/**
	 * Tests GET /concept
	 */
	@Test
	public void testGetAllConcepts() {
		try {
			mockMvcConceptController
			        .perform(get("/concept")
					.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());			
		} catch (Exception e) {
			fail();
		}
	}

	/**
     * Tests GET /project/{projectName}/concept
     */
    @Test
    public void testGetAllConceptsInProject() {
        try {
            mockMvcConceptController
                    .perform(get("/project/{projectName}/concept","testproject")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }
    
    /**
     * Tests GET /project/{projectName}/concept/{conceptName}
     */
    @Test
    public void testGetConcept() {
        try {
            mockMvcConceptController
                    .perform(get("/project/{projectName}/concept/{name}","testproject","concept1")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }
	
	/**
	 * Tests POST /project/{projectName}/concept
	 */
	@Test
	public void testCreateConcept() {
		try {
			mockMvcConceptController.perform(post("/project/{projectName}/concept","testproject")
					.content("{\"name\": \"concept4\", \"definition\": {\"type\":\"textual\",\"data\":{\"text\":\"Example Definition\"}}, \"attributes\": [\"attr4\"]}")
					.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
     * Tests DELETE /project/{projectName}/concept/{conceptName}
     */
	@Test
	public void testRemoveConcept() {
		try {
			mockMvcConceptController
			        .perform(delete("/project/{projectName}/concept/{conceptName}","testproject","concept2")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isNoContent());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
     * Tests PATCH /project/{projectName}/concept/{conceptName}
     */
	@Test
	public void testUpdateConcept() {
		try {
			mockMvcConceptController
			        .perform(patch("/project/{projectName}/concept/{conceptName}","testproject","concept3")
							.content("{\"name\": \"concept3patch\", \"definition\": {\"type\":\"textual\",\"data\":{\"text\":\"Example Definition\"}}, \"attributes\": [\"attr3patch\"]}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}
}
