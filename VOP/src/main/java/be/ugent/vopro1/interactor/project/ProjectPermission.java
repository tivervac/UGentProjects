package be.ugent.vopro1.interactor.project;

import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.scheduling.Schedule;

/**
 * An object to interact with a database to retrieve permission options.
 * <p>
 * This handler serves as a way to handle permissions for various actions
 * on {@link EntityProject} objects. The adapter will use this to check if a
 * user has the required credentials to perform an action.
 *
 * @see EntityProject
 * @see be.ugent.vopro1.adapter.ProjectAdapter
 * @see be.ugent.vopro1.interactor.permission.PermissionProvider
 */
public interface ProjectPermission {

    /**
     * Checks if a user is allowed to add an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canAdd(String auth);

    /**
     * Checks if a user is allowed to edit an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that will be edited
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canEdit(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that will be retrieved
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGet(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve a list of {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canGetAll(String auth);

    /**
     * Checks if a user is allowed to search for {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canSearch(String auth);

    /**
     * Checks if a user is allowed to remove an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} that will be removed
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     */
    boolean canRemove(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve a list of analysts of an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} to retrieve analysts of
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see User
     */
    boolean canGetAnalysts(String auth, String projectName);

    /**
     * Checks if a user is allowed to add an analyst for an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} to add an analyst to
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see User
     */
    boolean canAddAnalyst(String auth, String projectName);

    /**
     * Checks if a user is allowed to edit an analyst for an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} to edit an analyst to
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see User
     */
    boolean canEditAnalyst(String auth, String projectName);

    /**
     * Checks if a user is allowed to remove an analyst from an {@link EntityProject}
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} to remove an analyst from
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see User
     */
    boolean canRemoveAnalyst(String auth, String projectName);

    /**
     * Checks if a user is allowed to retrieve the Schedule of an {@link EntityProject}.
     * Everyone can get this schedule.
     *
     * @param auth HTTP Authorization header for this user
     * @param projectName Name of the {@link EntityProject} to the Schedule of
     * @return <code>true</code> if permission is granted, <code>false</code> otherwise
     * @see EntityProject
     * @see Schedule
     */
    boolean canGetSchedule(String auth, String projectName);
}
