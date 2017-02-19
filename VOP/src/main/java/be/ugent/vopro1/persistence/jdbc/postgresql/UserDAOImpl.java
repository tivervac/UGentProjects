package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.jdbc.AbstractDAO;
import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;
import be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper.RowMapperProvider;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TableFactory;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectAnalystTable.ProjectAnalystColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectTable.ProjectColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamMemberTable.TeamMemberColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamTable.TeamColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable.UserColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable.UserColumn.*;

/**
 * DAO that communicates with the database about Users, represented by
 * PersistentUser.
 *
 * @see AbstractDAO
 * @see UserDAO
 * @see PersistentUser
 */
public class UserDAOImpl extends AbstractDAO implements UserDAO {

    private static final RowMapper<PersistentProject> PROJECT_MAPPER = RowMapperProvider.get(PersistentProject.class);
    private static final RowMapper<PersistentTeam> TEAM_MAPPER = RowMapperProvider.get(PersistentTeam.class);
    private static final RowMapper<PersistentUser> USER_MAPPER = RowMapperProvider.get(PersistentUser.class);
    private static final RowMapper<Long> PROJECT_ANALYST_MAPPER = RowMapperProvider.get(Long.class);

    private final QueryBuilderImpl queryBuilder = new QueryBuilderImpl();
    private final Table userTable = TableFactory.getInstance("user");
    private final Table teamTable = TableFactory.getInstance("team");
    private final Table memberTable = TableFactory.getInstance("member");
    private final Table projectTable = TableFactory.getInstance("project");
    private final Table analystTable = TableFactory.getInstance("analyst");

    /**
     * {@inheritDoc}
     *
     * @param user {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentUser save(PersistentUser user) {
        String query = queryBuilder.insertQuery(userTable);
        Object[] args = new Object[]{
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.isAdmin(),
        };

        jdbcTemplate.update(query, args);
        return getByEmail(user.getEmail());
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
    public PersistentUser getById(int id) {
        String query = queryBuilder.selectQuery(userTable, new UserColumn[]{ID});
        Object[] args = new Object[]{id};

        return jdbcTemplate.queryForObject(query, USER_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentUser
     */
    @Override
    public PersistentUser getByEmail(String email) {
        String query = queryBuilder.selectQuery(userTable, new UserColumn[]{EMAIL});
        Object[] args = new Object[]{email};

        return jdbcTemplate.queryForObject(query, USER_MAPPER, args);
    }

    @Override
    public boolean exists(int id) {
        String query = queryBuilder.existsQuery(userTable, new UserColumn[]{ID});
        Object[] args = new Object[]{id};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    @Override
    public boolean exists(String email) {
        String query = queryBuilder.existsQuery(userTable, new UserColumn[]{EMAIL});
        Object[] args = new Object[]{email};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public List<PersistentUser> getAll() {
        String query = queryBuilder.selectQuery(userTable, null);

        return jdbcTemplate.query(query, USER_MAPPER);
    }

    /**
     * {@inheritDoc}
     *
     * @param user {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public void update(PersistentUser user) {
        Object[] args = new Object[]{
            user.getFirstName(),
            user.getLastName(),
            user.isAdmin(),
            user.getId()
        };
        UserColumn[] columns = new UserColumn[]{
            FIRST_NAME,
            LAST_NAME,
            IS_ADMIN
        };
        UserColumn[] updatedColumns = IntStream.range(0, args.length - 1)
                .filter(i -> (args[i] != null))
                .mapToObj(i -> columns[i])
                .collect(Collectors.toList()).toArray(new UserColumn[0]);
        Object[] updatedArgs = Arrays.stream(args).filter(i -> i != null).toArray();

        if (updatedColumns.length <= 0) {
            return;
        }

        String query = queryBuilder.updateQuery(userTable, updatedColumns, new UserColumn[]{
            ID
        });

        jdbcTemplate.update(query, updatedArgs);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteById(int id) {
        String query = queryBuilder.deleteQuery(userTable, new UserColumn[]{ID});
        Object[] args = new Object[]{id};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @see PersistentUser
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteByEmail(String email) {
        String query = queryBuilder.deleteQuery(userTable, new UserColumn[]{EMAIL});
        Object[] args = new Object[]{email};

        jdbcTemplate.update(query, args);
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
                memberTable,
                teamTable,
                TeamMemberColumn.TEAM_ID,
                TeamColumn.ID,
                new TeamMemberColumn[]{TeamMemberColumn.USER_ID}
        );

        return jdbcTemplate.query(query, TEAM_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentTeam
     * @see #getAllTeamsById(int)
     */
    @Override
    public List<PersistentTeam> getAllTeamsByEmail(String email) {
        return getAllTeamsById(getByEmail(email).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param userId {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public long getWorkhours(int userId, int projectId) {
        String query = queryBuilder.selectQuery(
                analystTable,
                new Column[] {ProjectAnalystColumn.USER_ID, ProjectAnalystColumn.PROJECT_ID}
        );

        Object[] args = new Object[] {userId, projectId};

        return jdbcTemplate.queryForObject(query, PROJECT_ANALYST_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     */
    @Override
    public List<PersistentProject> getAllAnalystProjectsById(int id) {
        String query = queryBuilder.crossoverQuery(
                analystTable,
                projectTable,
                ProjectAnalystColumn.PROJECT_ID,
                ProjectColumn.ID,
                new ProjectAnalystColumn[]{ProjectAnalystColumn.USER_ID}
        );

        return jdbcTemplate.query(query, PROJECT_MAPPER, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentProject
     */
    @Override
    public List<PersistentProject> getAllAnalystProjectsByEmail(String email) {
        return getAllAnalystProjectsById(getByEmail(email).getId());
    }
}
