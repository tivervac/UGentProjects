package be.ugent.vopro1.interactor.user;

import be.ugent.vopro1.model.User;

/**
 * An object to interact with a database to retrieve permission options.
 * <p>
 * This handler serves as a way to handle permissions for various actions
 * on {@link User} objects. The adapter will use this to check if a
 * user has the required credentials to perform an action.
 *
 * @see User
 * @see be.ugent.vopro1.adapter.TeamAdapter
 * @see be.ugent.vopro1.interactor.permission.PermissionProvider
 */
public interface UserPermission {

    /**
     * Checks if someone is allowed to add a {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAdd(String auth);

    /**
     * Checks if a user is allowed to edit a {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @param userId Identifier of the user to edit.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canEdit(String auth, int userId);

    /**
     * Checks if a user is allowed to retrieve a {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @param userId Identifier of the user to retrieve.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGet(String auth, int userId);

    /**
     * Checks if a user is allowed to upgrade another user (make admin or analyst).
     *
     * @param auth HTTP authentication header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canUpgrade(String auth);

    /**
     * Checks if a user is allowed to retrieve a {@link User} of him/herself.
     *
     * @param auth HTTP Authorization header for this user.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetSelf(String auth);

    /**
     * Checks if a user is allowed to retrieve a list of {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetAll(String auth);

    /**
     * Checks if a user is allowed to remove a {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemove(String auth);

    /**
     * Checks if a user is allowed to retrieve a list of teams of a {@link User}.
     *
     * @param auth HTTP Authorization header for this user.
     * @param userId Identifier of the user to retrieve teams of
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetTeams(String auth, int userId);

    /**
     * Checks if a user is allowed to retrieve a list of projects that a {@link User}
     * is an analyst for.
     *
     * @param auth HTTP Authorization header for this user.
     * @param userId Identifier of the user to retrieve analyst projects of
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetAnalystList(String auth, int userId);
}
