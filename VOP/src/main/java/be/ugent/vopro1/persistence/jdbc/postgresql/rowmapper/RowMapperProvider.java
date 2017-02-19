package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.*;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the correct {@link RowMapper} based on the class to map to
 *
 * @see RowMapper
 * @see DocumentRowMapper
 * @see ProjectRowMapper
 * @see TeamRowMapper
 * @see UserRowMapper
 */
public class RowMapperProvider {

    private static Map<Class, RowMapper> mappers;
    static {
        setDefault();
    }

    /**
     * Retrieves the mapper for a certain bean type
     *
     * @param clazz Class of the bean type to map for
     * @param <T> Bean type
     * @return Requested RowMapper
     */
    public static <T> RowMapper<T> get(Class<T> clazz) {
        return mappers.get(clazz);
    }

    /**
     * Sets a custom mapper for a certain bean type
     *
     * @param clazz Class of the bean type to map for
     * @param mapper Mapper to use
     * @param <T> Bean type
     */
    public static <T> void put(Class<T> clazz, RowMapper<T> mapper) {
        mappers.put(clazz, mapper);
    }

    /**
     * Fills up the mappers HashMap with the default mapper for each
     * Persistent bean
     */
    public static void setDefault() {
        mappers = new HashMap<>();
        mappers.put(PersistentUser.class, new UserRowMapper());
        mappers.put(PersistentProject.class, new ProjectRowMapper());
        mappers.put(PersistentObject.class, new DocumentRowMapper());
        mappers.put(PersistentTeam.class, new TeamRowMapper());
        mappers.put(Long.class, new ProjectAnalystMapper());
        mappers.put(PersistentAssignment.class, new AssignmentMapper());
        mappers.put(PersistentTask.class, new TaskMapper());
    }
}
