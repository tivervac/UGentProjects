package be.ugent.vopro1.reference;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * Provides an immutable configuration object for {@link LookupStrategy}
 */
public class LookupConfig {

    private String entityName;
    private String projectName;
    private Class<? extends Declaration> clazz;

    /**
     * Creates a new LookupConfig with given parameters
     *
     * @param entityName Name of the entity
     * @param projectName Name of the project the entity resides in
     * @param clazz Class of the entity
     */
    private LookupConfig(String entityName, String projectName, Class<? extends Declaration> clazz) {
        this.entityName = entityName;
        this.projectName = projectName;
        this.clazz = clazz;
    }

    /**
     * Creates a new LookupConfig without parameters. Use {@link #entityName()}, {@link #projectName()} and
     * {@link #clazz()} to set the required parameters.
     */
    public LookupConfig() {
        // Empty on purpose
    }

    /**
     * Retrieves the entity name
     *
     * @return Entity name
     */
    public String entityName() {
        return entityName;
    }

    /**
     * Retrieves the entity project
     *
     * @return Entity project
     */
    public String projectName() {
        return projectName;
    }

    /**
     * Retrieves the entity class
     *
     * @return Entity class
     */
    public Class<? extends Declaration> clazz() {
        return clazz;
    }

    /**
     * Returns a new {@link LookupConfig} object with given entity name.
     * This method can be chained with other immutable setters like {@link #projectName()} and {@link #clazz()}.
     *
     * @param entityName Entity name to use in the new configuration
     * @return New configuration with given entity name
     */
    public LookupConfig entityName(String entityName) {
        return new LookupConfig(entityName, this.projectName, this.clazz);
    }

    /**
     * Returns a new {@link LookupConfig} object with given project name.
     * This method can be chained with other immutable setters like {@link #entityName()} and {@link #clazz()}.
     *
     * @param projectName Project name to use in the new configuration
     * @return New configuration with given project name
     */
    public LookupConfig projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    /**
     * Returns a new {@link LookupConfig} object with given entity class.
     * This method can be chained with other immutable setters like {@link #entityName()} and {@link #projectName()}.
     *
     * @param clazz Entity class to use in the new configuration
     * @return New configuration with given entity class
     */
    public LookupConfig clazz(Class<? extends Declaration> clazz) {
        this.clazz = clazz;
        return this;
    }

}
