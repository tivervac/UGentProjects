package be.ugent.vopro1.interactor.team;

import be.ugent.vopro1.model.Team;

/**
 * An object to interact with a database to retrieve permission options.
 * <p>
 * This handler serves as a way to handle permissions for various actions
 * on {@link Team} objects. The adapter will use this to check if a
 * user has the required credentials to perform an action.
 *
 * @see Team
 * @see be.ugent.vopro1.adapter.TeamAdapter
 * @see be.ugent.vopro1.interactor.permission.PermissionProvider
 */
public interface TeamPermission {

    /**
     * Checks if a user is allowed to add a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAdd(String auth);

    /**
     * Checks if a user is allowed to edit a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to be edited
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canEdit(String auth, int teamId);

    /**
     * Checks if a user is allowed to retrieve a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to be retrieved
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGet(String auth, int teamId);

    /**
     * Checks if a user is allowed to retrieve a list of {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetAll(String auth);

    /**
     * Checks if a user is allowed to remove a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to be removed
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemove(String auth, int teamId);

    /**
     * Checks if a user is allowed to retrieve a list of members of a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to retrieve members from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetMembers(String auth, int teamId);

    /**
     * Checks if a user is allowed to add a member to a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to add member to
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAddMember(String auth, int teamId);

    /**
     * Checks if a user is allowed to remove a member from a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to remove member from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemoveMember(String auth, int teamId);

    /**
     * Checks if a user is allowed to retrieve a list of projects of a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to retrieve projects from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetProjects(String auth, int teamId);

    /**
     * Checks if a user is allowed to add a project to a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to add project to
     * @param projectName Name of the project that should be added
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAddProject(String auth, int teamId, String projectName);

    /**
     * Checks if a user is allowed to remove a project from a {@link Team}.
     *
     * @param auth HTTP Authorization header for this user
     * @param teamId Identifier of the team to remove project from
     * @param projectName Name of the project that should be removed
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemoveProject(String auth, int teamId, String projectName);
}
