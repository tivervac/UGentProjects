package be.ugent.vopro1.controllers;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.interactor.mocks.ProjectPermissionMock;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.rest.controller.ActorRestController;
import be.ugent.vopro1.rest.controller.ConceptRestController;
import org.aikodi.chameleon.workspace.ProjectException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import be.ugent.vopro1.rest.controller.ProjectRestController;

import javax.sql.DataSource;
import java.util.Base64;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/spring_test.xml")
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_projects.sql"})
public class TestProject {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvcProjectController;
	String basicHeader;

	@Before
	public void setup() throws ProjectException {
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvcProjectController = MockMvcBuilders.standaloneSetup(new ProjectRestController()).build();
		Base64.Encoder encoder = Base64.getEncoder();
		basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
		CustomWorkspace workspace = WorkspaceFactory.getInstance();

		workspace.createProject("project1");
		workspace.createProject("project2");
		workspace.createProject("project3");
	}

	/**
	 * TEST GET /project
	 */
	@Test
	public void testGetAllProjects() {
		try {
			mockMvcProjectController
			        .perform(get("/project").header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());			
		} catch (Exception e) {
			fail();
		}
	}
    
    /**
     * Tests GET /project/{projectName}
     */
    @Test
    public void testGetProject() {
        try {
            mockMvcProjectController
                    .perform(get("/project/{projectName}","project1").header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

	/**
	 * Tests POST /project.
	 */
	@Test
	public void testCreateProject() {
		try {
			mockMvcProjectController
			        .perform(post("/project")
							.header(HttpHeaders.AUTHORIZATION, basicHeader)
							.content("{\"name\": \"project43\", \"leader\": {\"email\": \"test@test.com\"}}"))
					.andExpect(status().isCreated());
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Tests DELETE /project/{projectName}
	 */
	@Test
	public void testRemoveProject() {
		try {
			mockMvcProjectController
			        .perform(delete("/project/{projectName}","project2")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isNoContent());
		} catch (Exception e) {
			fail();
		}
	}	
	
	/**
	 * Tests PATCH /project/{projectName}
	 */
	@Test
	public void testUpdateProject() {
		try {
			mockMvcProjectController
			        .perform((patch("/project/{projectName}","project3")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
							.content("{\"name\": \"project3patch\", \"leader\": {\"email\": \"test@test.com\"}}"))
					.andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}

}
