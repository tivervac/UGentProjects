package be.ugent.vopro1.interactor.entity;

import be.ugent.vopro1.model.EntityProject;

/**
 * An object to interact with a database to retrieve permission options.
 * <p>
 * This handler serves as a way to handle permissions for various actions
 * on objects. The adapter will use this to check if a
 * user has the required credentials to perform an action.
 *
 * @see be.ugent.vopro1.adapter.EntityAdapter
 * @see be.ugent.vopro1.interactor.permission.PermissionProvider
 */
public interface EntityPermission {

    /**
     * Checks if a user is allowed to add an entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entity will be added to
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAdd(String auth, String projectName);

    /**
     * Checks if a user is allowed to edit an entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entity will be edited in
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canEdit(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve an entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entity will be retrieved from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGet(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve a list of entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entities will be retrieved from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetAll(String auth, String projectName);

    /**
     * Checks if a user is allowed to remove an entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entity will be removed from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemove(String auth, String projectName);

    /**
     * Checks if a user is allowed to search for an entity.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the project that the entity will be searched in for
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canSearch(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve a list of analysts of a {@link be.ugent.vopro1.model.UsecaseEntity}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that contains the entity to retrieve analysts of
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see be.ugent.vopro1.model.User
     */
    boolean canGetAnalysts(String auth, String projectName);

    /**
     * Checks if a user is allowed to add an analyst for a {@link be.ugent.vopro1.model.UsecaseEntity}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that contains the entity to add an analyst to
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see be.ugent.vopro1.model.User
     */
    boolean canAddAnalyst(String auth, String projectName);

    /**
     * Checks if a user is allowed to remove an analyst from a {@link be.ugent.vopro1.model.UsecaseEntity}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that contains the entity to remove an analyst from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see be.ugent.vopro1.model.User
     */
    boolean canRemoveAnalyst(String auth, String projectName);
}
