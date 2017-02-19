package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.jdbc.AbstractDAO;
import be.ugent.vopro1.persistence.jdbc.Table;
import be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper.RowMapperProvider;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TableFactory;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectAnalystTable.ProjectAnalystColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectTable.ProjectColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamProjectTable.TeamProjectColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamTable.TeamColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable.UserColumn;

/**
 * DAO that communicates with the database about Projects represented by
 * PersistentProjects.
 *
 * @see AbstractDAO
 * @see ProjectDAO
 * @see PersistentProject
 * @see QueryBuilderImpl
 * @see Table
 */
public class ProjectDAOImpl extends AbstractDAO implements ProjectDAO {

    private static final RowMapper<PersistentProject> PROJECT_MAPPER = RowMapperProvider.get(PersistentProject.class);
    private static final RowMapper<PersistentTeam> TEAM_MAPPER = RowMapperProvider.get(PersistentTeam.class);
    private static final RowMapper<PersistentUser> USER_MAPPER = RowMapperProvider.get(PersistentUser.class);

    private final QueryBuilderImpl queryBuilder = new QueryBuilderImpl();
    private final Table projectTable = TableFactory.getInstance("project");
    private final Table teamProjectTable = TableFactory.getInstance("teamProject");
    private final Table teamTable = TableFactory.getInstance("team");
    private final Table analystTable = TableFactory.getInstance("analyst");
    private final Table userTable = TableFactory.getInstance("user");

    /**
     * {@inheritDoc}
     *
     * @param persistentProject {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentProject save(PersistentProject persistentProject) {
        String query = queryBuilder.insertQuery(projectTable);
        Object[] args = new Object[]{
                persistentProject.getName(),
                persistentProject.getLeaderId()
        };

        jdbcTemplate.update(query, args);
        return getByName(persistentProject.getName());
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentProject getByName(String name) {
        String query = queryBuilder.selectQuery(projectTable, new ProjectColumn[]{ProjectColumn.NAME});
        Object[] args = new Object[]{name};

        return jdbcTemplate.queryForObject(query, PROJECT_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentProject getById(int id) {
        String query = queryBuilder.selectQuery(projectTable, new ProjectColumn[]{ProjectColumn.ID});
        Object[] args = new Object[]{id};

        return jdbcTemplate.queryForObject(query, PROJECT_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param persistentProject {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public void update(PersistentProject persistentProject) {
        String query = queryBuilder.updateQuery(projectTable, new ProjectColumn[]{ProjectColumn.NAME, ProjectColumn.LEADER_ID},
                new ProjectColumn[]{ProjectColumn.ID});
        Object[] args = new Object[]{
                persistentProject.getName(),
                persistentProject.getLeaderId(),
                persistentProject.getId()
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteByName(String name) {
        String query = queryBuilder.deleteQuery(projectTable, new ProjectColumn[]{ProjectColumn.NAME});
        Object[] args = new Object[]{name};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteById(int id) {
        String query = queryBuilder.deleteQuery(projectTable, new ProjectColumn[]{ProjectColumn.ID});
        Object[] args = new Object[]{id};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #search(String query)} with an empty query.
     *
     * @return a list of all the PersistentProjects in the project database
     * @see PersistentProject
     * @see QueryBuilderImpl
     * @see #search(String query)
     */
    @Override
    public List<PersistentProject> getAll() {
        return search("");
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    @Override
    public boolean exists(int id) {
        String query = queryBuilder.existsQuery(projectTable, new ProjectColumn[]{ProjectColumn.ID});
        Object[] args = new Object[]{id};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public boolean exists(String name) {
        String query = queryBuilder.existsQuery(projectTable, new ProjectColumn[]{ProjectColumn.NAME});
        Object[] args = new Object[]{name};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #search(String query)}
     *
     * @param query {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see QueryBuilderImpl
     * @see #search(String query)
     */
    @Override
    public List<PersistentProject> find(String query) {
        return search(query);
    }

    /**
     * Find a list of PersistentProjects described by a query.
     * <p>
     * An example query would be "Pro" where find would return every
     * PersistentProject starting with "Pro", such as Project and Projects.
     *
     * @param query a query that describes the PersistentProjects you wish to
     *              find
     * @return a list of all PersistentProjects described by the query
     * @see PersistentProject
     * @see QueryBuilderImpl
     */
    private List<PersistentProject> search(String query) {
        String dbQuery = queryBuilder.regexQuery(projectTable, ProjectColumn.NAME, null);
        Object[] args = new Object[]{query};
        return jdbcTemplate.query(dbQuery, PROJECT_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentTeam> getAllTeamsById(int id) {
        String query = queryBuilder.crossoverQuery(
                teamProjectTable,
                teamTable,
                TeamProjectColumn.TEAM_ID,
                TeamColumn.ID,
                new TeamProjectColumn[]{TeamProjectColumn.PROJECT_ID}
        );

        return jdbcTemplate.query(query, TEAM_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see #getAllTeamsById(int)
     * @see PersistentTeam
     */
    @Override
    public List<PersistentTeam> getAllTeamsByName(String name) {
        return getAllTeamsById(getByName(name).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentUser> getAllAnalystsById(int id) {
        String query = queryBuilder.crossoverQuery(
                analystTable,
                userTable,
                ProjectAnalystColumn.USER_ID,
                UserColumn.ID,
                new ProjectAnalystColumn[]{ProjectAnalystColumn.PROJECT_ID}
        );

        return jdbcTemplate.query(query, USER_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see #getAllAnalystsById(int)
     * @see PersistentUser
     */
    @Override
    public List<PersistentUser> getAllAnalystsByName(String name) {
        return getAllAnalystsById(getByName(name).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId {@inheritDoc}
     * @param userId    {@inheritDoc}
     * @param workload  {@inheritDoc}
     * @see QueryBuilderImpl
     */
    @Override
    public void addAnalyst(int projectId, int userId, long workload) {
        String query = queryBuilder.insertQuery(analystTable);
        Object[] args = new Object[]{
                projectId,
                userId,
                workload
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId {@inheritDoc}
     * @param userId    {@inheritDoc}
     * @param workload  {@inheritDoc}
     */
    @Override
    public void editAnalyst(int projectId, int userId, long workload) {
        ProjectAnalystColumn[] updateArgs = new ProjectAnalystColumn[]{
                ProjectAnalystColumn.WORKHOURS
        };
        ProjectAnalystColumn[] whereArgs = new ProjectAnalystColumn[]{
                ProjectAnalystColumn.PROJECT_ID,
                ProjectAnalystColumn.USER_ID
        };
        String query = queryBuilder.updateQuery(analystTable, updateArgs, whereArgs);
        Object[] args = new Object[]{
                workload,
                projectId,
                userId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId {@inheritDoc}
     * @param userId    {@inheritDoc}
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteAnalyst(int projectId, int userId) {
        String query = queryBuilder.deleteQuery(analystTable, new ProjectAnalystColumn[]{ProjectAnalystColumn.PROJECT_ID, ProjectAnalystColumn.USER_ID});
        Object[] args = new Object[]{
                projectId,
                userId
        };

        jdbcTemplate.update(query, args);
    }
}
