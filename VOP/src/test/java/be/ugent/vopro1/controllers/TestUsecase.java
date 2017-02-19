package be.ugent.vopro1.controllers;

import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.funky.mocks.CustomWorkspaceMock;
import be.ugent.vopro1.rest.controller.ProjectRestController;
import be.ugent.vopro1.rest.controller.UsecaseRestController;
import org.aikodi.chameleon.workspace.LanguageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_document.sql", "create_actors.sql", "create_concepts.sql",
		"create_usecases.sql"})
public class TestUsecase {

	@Autowired
    private WebApplicationContext webApplicationContext;
	@Autowired
	private DataSource dataSource;
	String basicHeader;

    private CustomWorkspaceMock workspaceMock;
	private MockMvc mockMvcProjectController;
	private MockMvc mockMvcUsecaseController;

	@Before
	public void setup() throws Exception {
		MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvcProjectController = MockMvcBuilders.standaloneSetup(new ProjectRestController()).build();
		mockMvcUsecaseController = MockMvcBuilders.standaloneSetup(new UsecaseRestController()).build();
		Base64.Encoder encoder = Base64.getEncoder();
		basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
        workspaceMock = new CustomWorkspaceMock(new LanguageRepository());
        WorkspaceFactory.setInstance(workspaceMock);
		workspaceMock.createProject("testproject");
	}

    @After
    public void teardown() {
		WorkspaceFactory.setDefault();
    }

	/**
     * Tests GET /usecase
     */
	@Test
	public void testGetAllUsecases() {
		try {
			mockMvcUsecaseController
			        .perform(get("/usecase")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}

	/**
     * Tests GET /project/{projectName}/usecase
     */
	@Test
	public void testGetAllUsecasesInProject() {
		try {
			mockMvcUsecaseController
			        .perform(get("/project/{projectName}/usecase","testproject")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}

    /**
     * Tests GET /project/{projectName}/usecase/{usecaseName}
     */
    @Test
    public void testGetUsecase() {
        try {
            mockMvcUsecaseController
                    .perform(get("/project/{projectName}/usecase/{usecaseName}", "testproject", "usecase1")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

	/**
     * Tests POST /project/{projectName}/usecase
     */
	@Test
	public void testCreateUsecase() {
		try {
			mockMvcUsecaseController
			        .perform(post("/project/{projectName}/usecase", "testproject")
							.content("{\n" +
									"  \"actors\" : [ \"mike\" ],\n" +
									"  \"alternativeFlows\" : [ {\n" +
									"    \"condition\" : {\n" +
									"      \"type\" : \"textual\",\n" +
									"      \"data\" : {\n" +
									"        \"text\" : \"We feeling alternate\"\n" +
									"      }\n" +
									"    },\n" +
									"    \"behavior\" : {\n" +
									"      \"behaviors\" : [ {\n" +
									"        \"name\" : \"label4\",\n" +
									"        \"type\" : \"textualStep\",\n" +
									"        \"behavior\" : {\n" +
									"          \"executor\" : \"mike\",\n" +
									"          \"description\" : {\n" +
									"            \"type\" : \"textual\",\n" +
									"            \"data\" : {\n" +
									"              \"text\" : \"This is a textual step in the alt flow\"\n" +
									"            }\n" +
									"          }\n" +
									"        }\n" +
									"      } ]\n" +
									"    },\n" +
									"    \"name\" : \"Alt\"\n" +
									"  } ],\n" +
									"  \"concepts\" : [ ],\n" +
									"  \"exceptionalFlows\" : [ {\n" +
									"    \"condition\" : {\n" +
									"      \"type\" : \"textual\",\n" +
									"      \"data\" : {\n" +
									"        \"text\" : \"We feeling exceptional\"\n" +
									"      }\n" +
									"    },\n" +
									"    \"behavior\" : {\n" +
									"      \"behaviors\" : [ {\n" +
									"        \"name\" : \"label5\",\n" +
									"        \"type\" : \"textualStep\",\n" +
									"        \"behavior\" : {\n" +
									"          \"executor\" : \"mike\",\n" +
									"          \"description\" : {\n" +
									"            \"type\" : \"textual\",\n" +
									"            \"data\" : {\n" +
									"              \"text\" : \"This is a textual step in the exc flow\"\n" +
									"            }\n" +
									"          }\n" +
									"        }\n" +
									"      } ]\n" +
									"    },\n" +
									"    \"name\" : \"Exc\"\n" +
									"  } ],\n" +
									"  \"normalFlow\" : {\n" +
									"    \"behaviors\" : [ {\n" +
									"      \"name\" : \"label2\",\n" +
									"      \"type\" : \"textualStep\",\n" +
									"      \"behavior\" : {\n" +
									"        \"executor\" : \"mike\",\n" +
									"        \"description\" : {\n" +
									"          \"type\" : \"textual\",\n" +
									"          \"data\" : {\n" +
									"            \"text\" : \"This is a textual step\"\n" +
									"          }\n" +
									"        }\n" +
									"      }\n" +
									"    } ]\n" +
									"  },\n" +
									"  \"name\" : \"Usecase\",\n" +
									"  \"objective\" : {\"type\":\"textual\",\"data\":{\"text\":\"Example Objective\"}},\n" +
									"  \"preconditions\" : [ ],\n" +
									"  \"postconditions\" : [ ],\n" +
									"  \"process\" : \"\"\n" +
									"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isCreated());
		} catch (Exception e) {
			fail();
		}
	}

	/**
     * Tests DELETE /project/{projectName}/usecase/{usecaseName}
     */
	@Test
	public void testRemoveUsecase() {
		try {
			mockMvcUsecaseController
			        .perform(delete("/project/{projectName}/usecase/{usecaseName}", "testproject", "usecase2")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isNoContent());
		} catch (Exception e) {
			fail();
		}
	}

	/**
     * Tests PATCH /project/{projectName}/usecase/{usecaseName}
     */
	@Test
	public void testUpdateUsecase() {
		try {
			mockMvcUsecaseController
			        .perform(patch("/project/{projectName}/usecase/usecase1", "testproject")
							.content("{\n" +
									"  \"actors\" : [ \"mike\" ],\n" +
									"  \"alternativeFlows\" : [ {\n" +
									"    \"condition\" : {\n" +
									"      \"type\" : \"textual\",\n" +
									"      \"data\" : {\n" +
									"        \"text\" : \"We feeling alternate\"\n" +
									"      }\n" +
									"    },\n" +
									"    \"behavior\" : {\n" +
									"      \"behaviors\" : [ {\n" +
									"        \"name\" : \"label4\",\n" +
									"        \"type\" : \"textualStep\",\n" +
									"        \"behavior\" : {\n" +
									"          \"executor\" : \"mike\",\n" +
									"          \"description\" : {\n" +
									"            \"type\" : \"textual\",\n" +
									"            \"data\" : {\n" +
									"              \"text\" : \"This is a textual step in the alt flow\"\n" +
									"            }\n" +
									"          }\n" +
									"        }\n" +
									"      } ]\n" +
									"    },\n" +
									"    \"name\" : \"Alt\"\n" +
									"  } ],\n" +
									"  \"concepts\" : [ ],\n" +
									"  \"exceptionalFlows\" : [ {\n" +
									"    \"condition\" : {\n" +
									"      \"type\" : \"textual\",\n" +
									"      \"data\" : {\n" +
									"        \"text\" : \"We feeling exceptional\"\n" +
									"      }\n" +
									"    },\n" +
									"    \"behavior\" : {\n" +
									"      \"behaviors\" : [ {\n" +
									"        \"name\" : \"label5\",\n" +
									"        \"type\" : \"textualStep\",\n" +
									"        \"behavior\" : {\n" +
									"          \"executor\" : \"mike\",\n" +
									"          \"description\" : {\n" +
									"            \"type\" : \"textual\",\n" +
									"            \"data\" : {\n" +
									"              \"text\" : \"This is a textual step in the exc flow\"\n" +
									"            }\n" +
									"          }\n" +
									"        }\n" +
									"      } ]\n" +
									"    },\n" +
									"    \"name\" : \"Exc\"\n" +
									"  } ],\n" +
									"  \"normalFlow\" : {\n" +
									"    \"behaviors\" : [ {\n" +
									"      \"name\" : \"label2\",\n" +
									"      \"type\" : \"textualStep\",\n" +
									"      \"behavior\" : {\n" +
									"        \"executor\" : \"mike\",\n" +
									"        \"description\" : {\n" +
									"          \"type\" : \"textual\",\n" +
									"          \"data\" : {\n" +
									"            \"text\" : \"This is a textual step\"\n" +
									"          }\n" +
									"        }\n" +
									"      }\n" +
									"    } ]\n" +
									"  },\n" +
									"  \"name\" : \"Usecase\",\n" +
									"  \"objective\" : {\"type\":\"textual\",\"data\":{\"text\":\"Example Objective\"}},\n" +
									"  \"preconditions\" : [ ],\n" +
									"  \"postconditions\" : [ ],\n" +
									"  \"process\" : \"\"\n" +
									"}")
							.contentType(MediaType.APPLICATION_JSON)
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
			        .andExpect(status().isOk());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
    @Ignore("Funky is not very testable")
	public void testActionUsecase() {
		try {
			// Ensure the usecase is in the workspace, otherwise it might fail!
			WorkspaceFactory.getInstance().addEntityToProject(7, "testproject");

			mockMvcUsecaseController
					.perform(put("/project/{projectName}/usecase/{usecaseName}", "testproject", "usecase1")
							.content("{\"action\": \"validation\"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());

			mockMvcUsecaseController
					.perform(put("/project/{projectName}/usecase/{usecaseName}", "testproject", "usecase1")
							.content("{\"action\": \"story\"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());
            /*
             * Disabled because funky is not very testable
             *
			mockMvcUsecaseController
					.perform(put("/project/{projectName}/usecase/{usecaseName}", "testproject", "usecase1")
							.content("{\"action\": \"copy\", \"destination\": \"testproject2\"}")
							.header(HttpHeaders.AUTHORIZATION, basicHeader))
					.andExpect(status().isOk());
			*/
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
