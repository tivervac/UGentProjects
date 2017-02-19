package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.jdbc.AbstractDAO;
import be.ugent.vopro1.persistence.jdbc.Table;
import be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper.RowMapperProvider;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TableFactory;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectTable.ProjectColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamMemberTable.TeamMemberColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamProjectTable.TeamProjectColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamTable.TeamColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable.UserColumn;

/**
 * DAO that communicates with the database about Teams, represented by
 * PersistentTeams.
 *
 * @see AbstractDAO
 * @see TeamDAO
 * @see PersistentTeam
 * @see QueryBuilderImpl
 * @see Table
 */
public class TeamDAOImpl extends AbstractDAO implements TeamDAO {

    private static final RowMapper<PersistentProject> PROJECT_MAPPER = RowMapperProvider.get(PersistentProject.class);
    private static final RowMapper<PersistentTeam> TEAM_MAPPER = RowMapperProvider.get(PersistentTeam.class);
    private static final RowMapper<PersistentUser> USER_MAPPER = RowMapperProvider.get(PersistentUser.class);

    private final QueryBuilderImpl queryBuilder = new QueryBuilderImpl();
    private final Table teamTable = TableFactory.getInstance("team");
    private final Table memberTable = TableFactory.getInstance("member");
    private final Table userTable = TableFactory.getInstance("user");
    private final Table teamProjectTable = TableFactory.getInstance("teamProject");
    private final Table projectTable = TableFactory.getInstance("project");

    /**
     * {@inheritDoc}
     *
     * @param team {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentTeam save(PersistentTeam team) {
        String query = queryBuilder.insertQuery(teamTable);
        Object[] args = new Object[]{
            team.getName(),
            team.getLeaderId()
        };

        jdbcTemplate.update(query, args);
        return getByName(team.getName());
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
    public PersistentTeam getById(int id) {
        String query = queryBuilder.selectQuery(teamTable, new TeamColumn[]{TeamColumn.ID});
        Object[] args = new Object[]{id};

        return jdbcTemplate.queryForObject(query, TEAM_MAPPER, args);
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
    public PersistentTeam getByName(String name) {
        String query = queryBuilder.selectQuery(teamTable, new TeamColumn[]{TeamColumn.NAME});
        Object[] args = new Object[]{name};

        return jdbcTemplate.queryForObject(query, TEAM_MAPPER, args);
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
    public boolean exists(int id) {
        String query = queryBuilder.existsQuery(teamTable, new TeamColumn[]{TeamColumn.ID});
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
        String query = queryBuilder.existsQuery(teamTable, new TeamColumn[]{TeamColumn.NAME});
        Object[] args = new Object[]{name};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentTeam> getAll() {
        String query = queryBuilder.selectQuery(teamTable, null);

        return jdbcTemplate.query(query, TEAM_MAPPER);
    }

    /**
     * {@inheritDoc}
     *
     * @param team {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void update(PersistentTeam team) {
        String query = queryBuilder.updateQuery(teamTable, new TeamColumn[]{
            TeamColumn.NAME,
            TeamColumn.LEADER_ID
        }, new TeamColumn[]{
            TeamColumn.ID
        });
        Object[] args = new Object[]{
            team.getName(),
            team.getLeaderId(),
            team.getId()
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteById(int id) {
        String query = queryBuilder.deleteQuery(teamTable, new TeamColumn[]{TeamColumn.ID});
        Object[] args = new Object[]{id};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteByName(String name) {
        String query = queryBuilder.deleteQuery(teamTable, new TeamColumn[]{TeamColumn.NAME});
        Object[] args = new Object[]{name};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentUser
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentUser> getAllMembersById(int id) {
        String query = queryBuilder.crossoverQuery(
                memberTable,
                userTable,
                TeamMemberColumn.USER_ID,
                UserColumn.ID,
                new TeamMemberColumn[]{TeamMemberColumn.TEAM_ID}
        );

        return jdbcTemplate.query(query, USER_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see #getAllMembersById(int)
     * @see PersistentUser
     * @see PersistentTeam
     * @see QueryBuilderImpl
     * @see #getAllMembersById(int)
     */
    @Override
    public List<PersistentUser> getAllMembersByName(String name) {
        return getAllMembersById(getByName(name).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param userId {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void addMember(int teamId, int userId) {
        String query = queryBuilder.insertQuery(memberTable);
        Object[] args = new Object[]{
            teamId,
            userId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param userId {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteMember(int teamId, int userId) {
        String query = queryBuilder.deleteQuery(memberTable, new TeamMemberColumn[]{TeamMemberColumn.TEAM_ID, TeamMemberColumn.USER_ID});
        Object[] args = new Object[]{
            teamId,
            userId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentProject> getAllProjectsById(int id) {
        String query = queryBuilder.crossoverQuery(
                teamProjectTable,
                projectTable,
                TeamProjectColumn.PROJECT_ID,
                ProjectColumn.ID,
                new TeamProjectColumn[]{TeamProjectColumn.TEAM_ID}
        );

        return jdbcTemplate.query(query, PROJECT_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see #getAllProjectsById(int)
     * @see PersistentProject
     * @see PersistentTeam
     * @see #getAllProjectsById(int)
     */
    @Override
    public List<PersistentProject> getAllProjectsByName(String name) {
        return getAllProjectsById(getByName(name).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void addProject(int teamId, int projectId) {
        String query = queryBuilder.insertQuery(teamProjectTable);
        Object[] args = new Object[]{
            teamId,
            projectId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @see PersistentTeam
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteProject(int teamId, int projectId) {
        String query = queryBuilder.deleteQuery(teamProjectTable, new TeamProjectColumn[]{TeamProjectColumn.TEAM_ID, TeamProjectColumn.PROJECT_ID});
        Object[] args = new Object[]{
            teamId,
            projectId
        };

        jdbcTemplate.update(query, args);
    }
}
