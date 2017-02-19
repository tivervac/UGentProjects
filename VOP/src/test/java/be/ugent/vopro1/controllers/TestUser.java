package be.ugent.vopro1.controllers;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.rest.controller.UserRestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.util.Base64;

import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
@WebAppConfiguration
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_document.sql", "create_actors.sql", "create_concepts.sql",
        "create_usecases.sql"})
public class TestUser {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DataSource dataSource;
    String basicHeader;

    private MockMvc mockMvcUserController;

    @Before
    public void setup() throws Exception {
        MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvcUserController = MockMvcBuilders.standaloneSetup(new UserRestController()).build();
        Base64.Encoder encoder = Base64.getEncoder();
        basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
        CustomWorkspace workspace = WorkspaceFactory.getInstance();
        workspace.createProject("testproject");
    }

    @Test
    public void testGetAllUsers() {
        try {
            mockMvcUserController.perform(get("/user")
                .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUser() {
        String specificPath = "http://vopro1.ugent.be/api/user/1";
        String generalPath = "http://vopro1.ugent.be/api/user";

        try {
            mockMvcUserController.perform(get("/user/{userId}", "1")
                .with(request -> {
                    // Required for HATEOAS testing
                    request.setServletPath("/user/1");
                    return request;
                })
                .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id", is(1)))
                    .andExpect(jsonPath("$.content.firstName", is("test")))
                    .andExpect(jsonPath("$.content.lastName", is("test")))
                    .andExpect(jsonPath("$.content.email", is("test@test.com")))
                    .andExpect(jsonPath("$.content.admin", is(true)))
                    .andExpect(jsonPath("$.content.links.all", is(generalPath)))
                    .andExpect(jsonPath("$.content.links.create", is(generalPath)))
                    .andExpect(jsonPath("$.content.links.patch", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.analyst_projects", is(specificPath + "/project?analyst=true")))
                    .andExpect(jsonPath("$.content.links.teams", is(specificPath + "/team")))
                    .andExpect(jsonPath("$.content.links.self", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.delete", is(specificPath)))
                    .andDo(print());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddUser() {
        try {
            mockMvcUserController.perform(post("/user")
                    .content("{\n" +
                            "    \"firstName\": \"Test\",\n" +
                            "    \"lastName\": \"McTester\",\n" +
                            "    \"email\": \"test3@test.com\",\n" +
                            "    \"password\": \"hahano\",\n" +
                            "    \"admin\": false\n" +
                            "}"))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath("$.content.id", is(3)))
                    .andExpect(jsonPath("$.content.email", is("test3@test.com")));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateUser() {
        String specificPath = "http://vopro1.ugent.be/api/user/1";
        String generalPath = "http://vopro1.ugent.be/api/user";

        try {
            mockMvcUserController.perform(patch("/user/{userId}", "1")
                    .with(request -> {
                        // Required for HATEOAS testing
                        request.setServletPath("/user/1");
                        return request;
                    })
                    .content("{\n" +
                            "    \"firstName\": \"Test-Renamed\",\n" +
                            "    \"lastName\": \"McTester-Renamed\",\n" +
                            "    \"admin\": true\n" +
                            "}")
                    .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id", is(1)))
                    .andExpect(jsonPath("$.content.firstName", is("Test-Renamed")))
                    .andExpect(jsonPath("$.content.lastName", is("McTester-Renamed")))
                    .andExpect(jsonPath("$.content.email", is("test@test.com")))
                    .andExpect(jsonPath("$.content.admin", is(true)))
                    .andExpect(jsonPath("$.content.links.all", is(generalPath)))
                    .andExpect(jsonPath("$.content.links.create", is(generalPath)))
                    .andExpect(jsonPath("$.content.links.patch", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.analyst_projects", is(specificPath + "/project?analyst=true")))
                    .andExpect(jsonPath("$.content.links.teams", is(specificPath + "/team")))
                    .andExpect(jsonPath("$.content.links.self", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.delete", is(specificPath)))
                    .andDo(print());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testRemoveUser() {
        try {
            mockMvcUserController.perform(delete("/user/1")
                    .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().is(204));
        } catch (Exception e) {
            fail();
        }
    }
}
