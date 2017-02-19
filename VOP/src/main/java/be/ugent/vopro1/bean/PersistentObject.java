package be.ugent.vopro1.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Provides a representation of a persistent Object.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersistentObject {

    private int id;
    private int projectId;
    private String name;
    private String type;
    private String blob;

    /**
     * Empty constructor for Jackson
     */
    public PersistentObject() {
    }

    /**
     * Creates a new PersistentObject with given identifier. This should only be
     * used in DAO classes, nowhere else!
     *
     * @param id the unique id used by the database
     * @param projectId the id of the project this PersistentObject belongs to
     * @param name the name of the PersistentObject, unique in its project
     * @param type the type of PersistentObject, specified by a class object
     * @param blob a JSON representation of the data, used in the database
     * @see be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl
     */
    public PersistentObject(int id, int projectId, String name, String type, String blob) {
        if (name == null || type == null){
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.type = type;
        this.blob = blob;
    }

    /**
     * Creates a new PersistentObject. Call
     * {@link be.ugent.vopro1.persistence.EntityDAO#save} to create it in the
     * database. After saving, the correct identifier will be set automatically.
     *
     * @param projectId the id of the project this PersistentObject belongs to
     * @param name the name of the PersistentObject, unique in its project
     * @param type the type of PersistentObject, specified by a class object
     * @param blob a JSON representation of the data, used in the database
     * @see be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl
     */
    public PersistentObject(int projectId, String name, String type, String blob) {
       /* if (name == null || type == null || blob == null){
            throw new IllegalArgumentException();
        }*/

        this.id = -1;
        this.projectId = projectId;
        this.name = name;
        this.type = type;
        this.blob = blob;
    }

    /**
     * Retrieves the identifier of the entity.
     *
     * @return Identifier of the entity
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier of the entity. Only use this in DAO classes, nowhere
     * else!
     *
     * @param id Identifier of the entity
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the identifier of the project this entity resides in.
     *
     * @return Identifier of the project this entity resides in
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Sets the project identifier of the entity.
     *
     * @param projectId Identifier of the project
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Retrieves the name of the entity.
     *
     * @return Name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the entity. Call
     * {@link be.ugent.vopro1.persistence.EntityDAO#update} to write this change
     * to the database.
     *
     * @param name New name of the entity to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type of the entity
     *
     * @return type of the entity
     */
    public String getType(){
        return type;
    }

    /**
     * Sets the type of the entity. This should only be used in DAO classes,
     * nowhere else!
     *
     * @param type Type of the entity to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the JSON blob of the entity.
     *
     * @return JSON blob of the entity
     */
    public String getBlob() {
        return blob;
    }

    /**
     * Sets the JSON blob of the entity. Call
     * {@link be.ugent.vopro1.persistence.EntityDAO#update} to write this change
     * to the database.
     *
     * @param blob New JSON blob of the entity to set
     */
    public void setBlob(String blob) {
        this.blob = blob;
    }

    /**
     * Formats all data of this PersistentObject into a readable format.
     *
     * @return the string representation of this PersistentObject
     */
    @Override
    public String toString() {
        return "PersistentObject{"
                + "id=" + id
                + ", projectId=" + projectId
                + ", name='" + name + '\''
                + ", type=" + type
                + ", blob='" + blob + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentObject that = (PersistentObject) o;

        if (getId() != that.getId()) {
            return false;
        }
        if (getProjectId() != that.getProjectId()) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getType() != that.getType()) {
            return false;
        }
        return !(getBlob() != null ? !getBlob().equals(that.getBlob()) : that.getBlob() != null);

    }

    @Override
    public int hashCode() {
        final int modifier = 31;
        final int zero = 0;
        int result = getId();
        result = modifier * result + getProjectId();
        result = modifier * result + (getName() != null ? getName().hashCode() : zero);
        result = modifier * result + (getType() != null ? getType().hashCode() : zero);
        result = modifier * result + (getBlob() != null ? getBlob().hashCode() : zero);
        return result;
    }
}
