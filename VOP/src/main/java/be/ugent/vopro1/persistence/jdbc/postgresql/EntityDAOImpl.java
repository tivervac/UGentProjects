package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.jdbc.AbstractDAO;
import be.ugent.vopro1.persistence.jdbc.Table;
import be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper.RowMapperProvider;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.EntityAnalystTable;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TableFactory;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable;
import org.aikodi.lang.funky.concept.Concept;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.EntityTable.EntityColumn;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.EntityTable.EntityColumn.NAME;
import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.EntityTable.EntityColumn.PROJECT;

/**
 * DAO that communicates with the database about Entities represented by
 * PersistentObjects.
 *
 * @see AbstractDAO
 * @see EntityDAO
 * @see PersistentObject
 * @see org.aikodi.lang.funky.executors.Actor
 * @see Concept
 * @see be.ugent.vopro1.model.UsecaseEntity
 */
public class EntityDAOImpl extends AbstractDAO implements EntityDAO {
    private static final RowMapper<PersistentObject> DOCUMENT_MAPPER = RowMapperProvider.get(PersistentObject.class);
    private static final RowMapper<PersistentUser> USER_MAPPER = RowMapperProvider.get(PersistentUser.class);
    private static final String PROJECT_DAO = "project";
    private static final String USECASE_TYPE = "usecase";
    private final QueryBuilderImpl queryBuilder = new QueryBuilderImpl();
    private final Table entityTable = TableFactory.getInstance("entity");
    private final Table userTable = TableFactory.getInstance("user");
    private final Table entityAnalystTable = TableFactory.getInstance("entityAnalyst");

    /**
     * {@inheritDoc}
     *
     * @param persistentObject {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentObject save(PersistentObject persistentObject) {
        String query = queryBuilder.insertQuery(entityTable);
        Object[] args = new Object[]{
            persistentObject.getProjectId(),
            persistentObject.getName(),
            persistentObject.getType(),
            persistentObject.getBlob()
        };

        jdbcTemplate.update(query, args);
        return getByName(persistentObject.getName(), persistentObject.getProjectId());
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @return contains the PersistentObject with the specified name and
     * projectId
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentObject getByName(String name, int projectId) {
        String query = queryBuilder.selectQuery(entityTable, new EntityColumn[]{EntityColumn.NAME, EntityColumn.PROJECT});
        Object[] args = new Object[]{name, projectId};

        return jdbcTemplate.queryForObject(query, DOCUMENT_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method uses {@link #getByName(java.lang.String, int)}.
     *
     * @param name {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see #getByName(java.lang.String, int)
     * @see ProjectDAO
     */
    @Override
    public PersistentObject getByName(String name, String projectName) {
        ProjectDAO dao = DAOProvider.get(PROJECT_DAO);
        int projectId = dao.getByName(projectName).getId();

        return getByName(name, projectId);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see QueryBuilderImpl
     */
    @Override
    public boolean exists(int id) {
        String query = queryBuilder.existsQuery(entityTable, new EntityColumn[]{EntityColumn.ID});
        Object[] args = new Object[]{id};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     * @see QueryBuilderImpl
     */
    @Override
    public boolean exists(String name, String projectName) {
        ProjectDAO dao = DAOProvider.get(PROJECT_DAO);
        int projectId = dao.getByName(projectName).getId();

        return exists(name, projectId);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @return {@inheritDoc}
     * @see QueryBuilderImpl
     */
    @Override
    public boolean exists(String name, int projectId) {
        String query = queryBuilder.existsQuery(entityTable, new EntityColumn[]{EntityColumn.NAME, EntityColumn.PROJECT});
        Object[] args = new Object[]{name, projectId};

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);

        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public PersistentObject getById(int id) {
        String query = queryBuilder.selectQuery(entityTable, new EntityColumn[]{EntityColumn.ID});
        Object[] args = new Object[]{id};

        return jdbcTemplate.queryForObject(query, DOCUMENT_MAPPER, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param persistentObject {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public void update(PersistentObject persistentObject) {
        Object[] args = new Object[]{
            persistentObject.getName(),
            persistentObject.getBlob(),
            persistentObject.getId()
        };
        EntityColumn[] columns = new EntityColumn[]{
            EntityColumn.NAME,
            EntityColumn.BLOB
        };
        EntityColumn[] updatedColumns = IntStream.range(0, args.length - 1)
                .filter(i -> (args[i] != null))
                .mapToObj(i -> columns[i])
                .collect(Collectors.toList()).toArray(new EntityColumn[0]);
        Object[] updatedArgs = Arrays.stream(args).filter(i -> i != null).toArray();

        if (updatedColumns.length <= 0) {
            return;
        }

        String query = queryBuilder.updateQuery(entityTable, updatedColumns, new EntityColumn[]{
            EntityColumn.ID
        });

        jdbcTemplate.update(query, updatedArgs);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param projectId {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteByName(String name, int projectId) {
        String query = queryBuilder.deleteQuery(entityTable, new EntityColumn[]{EntityColumn.NAME, EntityColumn.PROJECT});
        Object[] args = new Object[]{name, projectId};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method uses {@link #deleteByName(java.lang.String, int) }
     *
     * @param name {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see #deleteByName(java.lang.String, int)
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteByName(String name, String projectName) {
        ProjectDAO dao = DAOProvider.get(PROJECT_DAO);
        int projectId = dao.getByName(projectName).getId();

        deleteByName(name, projectId);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    @Override
    public void deleteById(int id) {
        String query = queryBuilder.deleteQuery(entityTable, new EntityColumn[]{EntityColumn.ID});
        Object[] args = new Object[]{id};

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #find(String query, String projectName)} with a
     * blank query and projectName.
     *
     * @return a list of all the PersistentObjects in the document database
     * @see #find(String query, String projectName)
     */
    @Override
    public List<PersistentObject> getAll() {
        return find("", "");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #find(String query, String projectName)} with a
     * blank query.
     *
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     * @see #find(String query, String projectName)
     */
    @Override
    public List<PersistentObject> getAllForProject(String projectName) {
        return find("", projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #find(String query, String projectName)} with a
     * blank projectName.
     *
     * @param query {@inheritDoc}
     * @return {@inheritDoc}
     * @see #find(String query, String projectName)
     */
    @Override
    public List<PersistentObject> find(String query) {
        return find(query, "");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method calls {@link #find(String query, String projectName)}.
     *
     * @param query a query that describes the PersistentObjects you wish to
     * find
     * @param projectName the name of the project in which you want to execute
     * the query
     * @return a list of all PersistentObjects described by the query and in the
     * project described by projectName
     * @see #find(String query, String projectName)
     */
    @Override
    public List<PersistentObject> findInProject(String projectName, String query) {
        return find(query, projectName);
    }

    @Override
    public List<PersistentUser> getAllAnalysts(String name, String projectName) {
        PersistentObject obj = getByName(name, projectName);

        String query = queryBuilder.crossoverQuery(
                entityAnalystTable,
                userTable,
                EntityAnalystTable.EntityAnalystColumn.USER_ID,
                UserTable.UserColumn.ID,
                new EntityAnalystTable.EntityAnalystColumn[]{EntityAnalystTable.EntityAnalystColumn.ENTITY_ID}
        );

        return jdbcTemplate.query(query, USER_MAPPER, obj.getId());
    }

    @Override
    public void addAnalyst(String name, String projectName, int userId) {
        PersistentObject obj = getByName(name, projectName);

        if (!obj.getType().equals(USECASE_TYPE)) {
            // Only allow assigning analysts to usecases!
            throw new IllegalArgumentException("Can only assign analyst status for use cases");
        }

        String query = queryBuilder.insertQuery(entityAnalystTable);
        Object[] args = new Object[]{
                obj.getId(),
                userId
        };

        jdbcTemplate.update(query, args);
    }

    @Override
    public void deleteAnalyst(String name, String projectName, int userId) {
        PersistentObject obj = getByName(name, projectName);

        if (!obj.getType().equals(USECASE_TYPE)) {
            // Only allow discharging analysts from usecases!
            throw new IllegalArgumentException("Can only discharge analyst from use cases");
        }

        String query = queryBuilder.deleteQuery(entityAnalystTable,
                new EntityAnalystTable.EntityAnalystColumn[]{EntityAnalystTable.EntityAnalystColumn.ENTITY_ID,
                        EntityAnalystTable.EntityAnalystColumn.USER_ID});
        Object[] args = new Object[]{
                obj.getId(),
                userId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * Find a list of PersistentObjects described by a query and a projectName.
     * <p>
     * An example query would be "Ent" where find would return every
     * PersistentObject starting with "Ent", such as Entity and Entities.
     * <p>
     * The projectName narrows the search to only find PersistentObjects
     * matching the query in the right Project.
     *
     * @param query a query that describes the PersistentObjects you wish to
     * find
     * @param projectName the name of the project in which you want to execute
     * the query
     * @return a list of all PersistentObjects described by the query and in the
     * project described by projectName
     * @see PersistentObject
     * @see org.aikodi.chameleon.core.declaration.Declaration
     * @see QueryBuilderImpl
     */
    private List<PersistentObject> find(String query, String projectName) {
        String dbQuery;
        Object[] args;

        if (!projectName.isEmpty()) {
            ProjectDAO dao = DAOProvider.get(PROJECT_DAO);
            int projectId = dao.getByName(projectName).getId();

            dbQuery = queryBuilder.regexQuery(entityTable, NAME, new EntityColumn[]{PROJECT});
            args = new Object[]{query, projectId};
        } else {
            dbQuery = queryBuilder.regexQuery(entityTable, NAME, null);
            args = new Object[]{query};
        }

        return jdbcTemplate.query(dbQuery, DOCUMENT_MAPPER, args);
    }
}
