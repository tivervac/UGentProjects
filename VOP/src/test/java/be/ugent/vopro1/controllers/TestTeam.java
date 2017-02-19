package be.ugent.vopro1.controllers;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.rest.controller.TeamRestController;
import be.ugent.vopro1.util.LocalConstants;
import net.minidev.json.JSONArray;
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

import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
@WebAppConfiguration
@Sql({"create_person.sql", "create_people.sql", "create_project.sql", "create_document.sql", "create_actors.sql", "create_concepts.sql",
        "create_usecases.sql", "create_team.sql", "create_teams.sql"})
public class TestTeam {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private DataSource dataSource;
    String basicHeader;

    private MockMvc mockMvcTeamController;
    String generalPath = "http://vopro1.ugent.be/api/team";

    @Before
    public void setup() throws Exception {
        MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvcTeamController = MockMvcBuilders.standaloneSetup(new TeamRestController()).build();
        Base64.Encoder encoder = Base64.getEncoder();
        basicHeader = "Basic " + new String(encoder.encode("test@test.com:lolcode".getBytes()));
        CustomWorkspace workspace = WorkspaceFactory.getInstance();
        workspace.createProject("testproject");
    }

    @Test
    public void testGetAllTeams() {
        try {
            mockMvcTeamController.perform(get("/team")
                .header(HttpHeaders.AUTHORIZATION, basicHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.page", isA(JSONArray.class)))
                .andExpect(jsonPath("$.content.page[0].name", is("team1")))
                .andExpect(jsonPath("$.content.page[0].id", is(1)))
                .andExpect(jsonPath("$.content.page[1].name", is("team2")))
                .andExpect(jsonPath("$.content.page[1].id", is(2)))
                .andExpect(jsonPath("$.content.links.next", is(nullValue())))
                .andExpect(jsonPath("$.content.links.previous", is(nullValue())))
                .andExpect(jsonPath("$.content.metadata.hasNextPage", is(false)))
                .andExpect(jsonPath("$.content.metadata.hasPreviousPage", is(false)))
                .andExpect(jsonPath("$.content.metadata.pageNumber", is(1)))
                .andExpect(jsonPath("$.content.metadata.lastPage", is(true)))
                .andExpect(jsonPath("$.content.metadata.firstPage", is(true)))
                .andExpect(jsonPath("$.content.metadata.pageSize", is(LocalConstants.PAGE)))
                .andExpect(jsonPath("$.content.metadata.size", is(2)))      // Total # of teams (2)
                .andExpect(jsonPath("$.content.metadata.length", is(2)))    // Number of teams in this page (2)
                .andExpect(jsonPath("$.links.all", is(generalPath)))
                .andExpect(jsonPath("$.links.create", is(generalPath)));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testTeam() {
        String specificPath = generalPath + "/1";

        try {
            mockMvcTeamController.perform(get("/team/{teamId}", "1")
                    .with(request -> {
                        // Required for HATEOAS testing
                        request.setServletPath("/team/1");
                        return request;
                    })
                    .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id", is(1)))
                    .andExpect(jsonPath("$.content.name", is("team1")))
                    .andExpect(jsonPath("$.content.links.patch", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.team_analysts", is(specificPath + "/member?analyst=true")))
                    .andExpect(jsonPath("$.content.links.members", is(specificPath + "/member")))
                    .andExpect(jsonPath("$.content.links.member_post", is(specificPath + "/member/{userId}")))
                    .andExpect(jsonPath("$.content.links.member_delete", is(specificPath + "/member/{userId}")))
                    .andExpect(jsonPath("$.content.links.project_post", is(specificPath + "/project/{projectName}")))
                    .andExpect(jsonPath("$.content.links.project_delete", is(specificPath + "/project/{projectName}")))
                    .andExpect(jsonPath("$.content.links.self", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.delete", is(specificPath)))
                    .andDo(print());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTeam() {
        try {
            mockMvcTeamController.perform(post("/team")
                    .header(HttpHeaders.AUTHORIZATION, basicHeader)
                    .content("{\"name\": \"team3\", \"leader\": {\"email\": \"test@test.com\"}}"))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath("$.content.id", is(3)))
                    .andExpect(jsonPath("$.content.name", is("team3")))
                    .andExpect(jsonPath("$.content.leader.email", is("test@test.com")));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUpdateTeam() {
        String specificPath = generalPath + "/2";

        try {
            mockMvcTeamController.perform(patch("/team/{teamId}", "2")
                    .with(request -> {
                        // Required for HATEOAS testing
                        request.setServletPath("/team/2");
                        return request;
                    })
                    .content("{\"name\": \"team2-renamed\", \"leader\": {\"email\": \"test@test.com\"}}")
                    .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.id", is(2)))
                    .andExpect(jsonPath("$.content.name", is("team2-renamed")))
                    .andExpect(jsonPath("$.content.leader.id", is(1)))
                    .andExpect(jsonPath("$.content.links.patch", is(specificPath)))
                    .andExpect(jsonPath("$.content.links.team_analysts", is(specificPath + "/member?analyst=true")))
                    .andExpect(jsonPath("$.content.links.members", is(specificPath + "/member")))
                    .andExpect(jsonPath("$.content.links.member_post", is(specificPath + "/member/{userId}")))
                    .andExpect(jsonPath("$.content.links.member_delete", is(specificPath + "/member/{userId}")))
                    .andExpect(jsonPath("$.content.links.project_post", is(specificPath + "/project/{projectName}")))
                    .andExpect(jsonPath("$.content.links.project_delete", is(specificPath + "/project/{projectName}")))
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
            mockMvcTeamController.perform(delete("/team/2")
                    .header(HttpHeaders.AUTHORIZATION, basicHeader))
                    .andExpect(status().is(204));
        } catch (Exception e) {
            fail();
        }
    }
}
