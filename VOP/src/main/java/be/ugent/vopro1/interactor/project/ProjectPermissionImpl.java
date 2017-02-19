package be.ugent.vopro1.interactor.project;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandler;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;

import java.util.List;

/**
 * An implementation of the {@link ProjectPermission} interface with a few sensible
 * default permission settings.
 */
public class ProjectPermissionImpl implements ProjectPermission {

    private UserDAO userDAO;
    private TeamDAO teamDAO;
    private ProjectDAO projectDAO;
    private HeaderAuthenticationHandler authHandler;

    /**
     * Creates a new ProjectPermissionImpl and initializes some
     * services that it depends on
     */
    public ProjectPermissionImpl() {
        this.userDAO = DAOProvider.get("user");
        this.teamDAO = DAOProvider.get("team");
        this.projectDAO = DAOProvider.get("project");
        this.authHandler = AuthenticationFactory.getInstance("header");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canAdd(String auth) {
        return admin(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and project leaders are granted permission.
     */
    @Override
    public boolean canEdit(String auth, String projectName) {
        return admin(auth) || projectLeader(auth, projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins, project leaders, and users who are part of a team assigned to the project are granted permission.
     */
    @Override
    public boolean canGet(String auth, String projectName) {
        return admin(auth) || projectLeader(auth, projectName) || projectTeamMember(auth, projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canGetAll(String auth) {
        return admin(auth) || aTeamLeader(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canSearch(String auth) {
        return admin(auth) || aTeamLeader(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canRemove(String auth, String projectName) {
        return admin(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins, project leaders and users who are part of a team assigned to the project are granted permission.
     */
    @Override
    public boolean canGetAnalysts(String auth, String projectName) {
        return admin(auth) || projectTeamMember(auth, projectName) || projectLeader(auth, projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and project leaders are granted permission.
     */
    @Override
    public boolean canAddAnalyst(String auth, String projectName) {
        return admin(auth) || projectLeader(auth, projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and project leaders are granted permission.
     */
    @Override
    public boolean canEditAnalyst(String auth, String projectName) {
        return admin(auth) || projectLeader(auth, projectName);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and project leaders are granted permission.
     */
    @Override
    public boolean canRemoveAnalyst(String auth, String projectName) {
        return admin(auth) || projectLeader(auth, projectName);
    }

    /**
     * {@inheritDoc}
     *
     * @param auth {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean canGetSchedule(String auth, String projectName) {
        return true;
    }

    private boolean aTeamLeader(String auth) {
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);
        return teamDAO.getAll()
                .stream()
                .mapToInt(PersistentTeam::getLeaderId)
                .anyMatch((leaderId) -> leaderId == user.getId());
    }

    private boolean projectLeader(String auth, String projectName) {
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentProject project = projectDAO.getByName(projectName);
        PersistentUser user = userDAO.getByEmail(email);

        return authHandler.hasPermission(auth, true) && project.getLeaderId() == user.getId();
    }

    private boolean projectTeamMember(String auth, String projectName) {
        List<PersistentTeam> teams = projectDAO.getAllTeamsByName(projectName);
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);

        // OK if user is in at least one team assigned to this project
        return authHandler.hasPermission(auth, true) && teams.stream().anyMatch(team -> {
            List<PersistentUser> members = teamDAO.getAllMembersById(team.getId());
            return members.contains(user);
        });
    }

    private boolean admin(String auth) {
        return authHandler.hasPermission(auth, true, true);
    }
}
