package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a single instance of each Table associated with a database relation in
 * a Map.
 *
 * @see Table
 * @see Map
 */
public class TableFactory {

    private static Map<String, Table> instances;

    /**
     * Sets a custom map, for dependency injection and tests
     *
     * @param tables Map of the tables to use in future
     * {@link #getInstance(String type)} calls
     */
    public static void setInstance(Map<String, Table> tables) {
        instances = tables;
    }

    /**
     * Retrieves the Table that is responsible for the given database relation.
     * <p>
     * The default mapping is based on the beans defined is as follows:
     * <ul>
     * <li>"entity" -&gt; {@link EntityTable}</li>
     * <li>"project" -&gt; {@link ProjectTable}</li>
     * <li>"user" -&gt; {@link UserTable}</li>
     * <li>"team" -&gt; {@link TeamTable}</li>
     * <li>"member" -&gt; {@link TeamMemberTable}</li>
     * <li>"analyst" -&gt; {@link ProjectAnalystTable}</li>
     * <li>"teamProject" -&gt; {@link TeamProjectTable}</li>
     * <li>"entityAnalyst" -&gt; {@link EntityAnalystTable}</li>
     * </ul>
     *
     * @param type Class that should be adapted
     * @return CommonAdapter that can adapt the given class
     */
    public static Table getInstance(String type) {
        if (instances == null) {
            instances = new HashMap<>();

            instances.put("entity", new EntityTable());
            instances.put("project", new ProjectTable());
            instances.put("user", new UserTable());
            instances.put("team", new TeamTable());
            instances.put("member", new TeamMemberTable());
            instances.put("analyst", new ProjectAnalystTable());
            instances.put("teamProject", new TeamProjectTable());
            instances.put("entityAnalyst", new EntityAnalystTable());
            instances.put("task", new TaskTable());
            instances.put("assignment", new AssignmentTable());
        }

        return instances.get(type);
    }
}
