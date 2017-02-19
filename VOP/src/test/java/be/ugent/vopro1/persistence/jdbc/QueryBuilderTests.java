package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.persistence.jdbc.postgresql.QueryBuilderImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectTable;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamProjectTable;
import org.aikodi.chameleon.workspace.Project;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTests {

    private QueryBuilderImpl qb;

    @Before
    public void initialize() {
        qb = new QueryBuilderImpl();
    }

    @Test
    public void testInsert() {
        String expected = "insert into project (name, leader_id) values (?, ?)";
        assertEquals(expected, qb.insertQuery(new ProjectTable()));
    }

    @Test
    public void testRegex() {
        String expected = "select id, name, leader_id from project where name ~* ?";
        assertEquals(expected, qb.regexQuery(new ProjectTable(),
                ProjectTable.ProjectColumn.NAME,
                null));
        expected = "select id, name, leader_id from project where name ~* ? and id = ?";
        assertEquals(expected, qb.regexQuery(new ProjectTable(),
                ProjectTable.ProjectColumn.NAME,
                new ProjectTable.ProjectColumn[]{ProjectTable.ProjectColumn.ID}));
    }

    @Test
    public void testSelect() {
        String expected = "select id, name, leader_id from project";
        assertEquals(expected, qb.selectQuery(new ProjectTable(), null));
        expected = "select id, name, leader_id from project where id = ? and name = ?";
        assertEquals(expected,
                qb.selectQuery(new ProjectTable(),
                        new Column[]{ProjectTable.ProjectColumn.ID, ProjectTable.ProjectColumn.NAME}));
    }

    @Test
    public void testUpdate() {
        String expected = "update project set name = ? where id = ?";
        assertEquals(expected, qb.updateQuery(new ProjectTable(),
                new Column[]{ProjectTable.ProjectColumn.NAME},
                new Column[]{ProjectTable.ProjectColumn.ID}));
    }

    @Test
    public void testCrossover() {
        String expected = "select project.id, project.name, project.leader_id from team_project, project where team_project.project_id = "
                + "project.id and team_project.team_id = ?";
        String query = qb.crossoverQuery(
                new TeamProjectTable(),
                new ProjectTable(),
                TeamProjectTable.TeamProjectColumn.PROJECT_ID,
                ProjectTable.ProjectColumn.ID,
                new TeamProjectTable.TeamProjectColumn[]{TeamProjectTable.TeamProjectColumn.TEAM_ID});

        assertEquals(expected, query);
    }
}
