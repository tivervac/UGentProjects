package be.ugent.vopro1.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TeamTest {

    private Team team;

    @Before
    public void initialize() {
        team = Team.TeamBuilder.aTeam().name("team").build();
    }

    @Test
    public void getName() {
        assertEquals("team", team.getName());
    }

    @Test
    public void setName() {
        team.setName("team-renamed");
        assertEquals("team-renamed", team.getName());
    }

}
