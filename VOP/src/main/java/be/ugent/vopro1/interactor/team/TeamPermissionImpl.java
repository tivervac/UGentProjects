package be.ugent.vopro1.interactor.team;

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
 * An implementation of the {@link TeamPermission} interface with a few sensible
 * default permission settings.
 */
public class TeamPermissionImpl implements TeamPermission {

    private UserDAO userDAO;
    private TeamDAO teamDAO;
    private HeaderAuthenticationHandler authHandler;

    /**
     * Creates a new TeamPermissionImpl and initializes some
     * services that it depends on.
     */
    public TeamPermissionImpl() {
        this.userDAO = DAOProvider.get("user");
        this.teamDAO = DAOProvider.get("team");
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
     * Admins and team analysts are granted permission.
     */
    @Override
    public boolean canEdit(String auth, int teamId) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and team members (including analysts) are granted permission.
     */
    @Override
    public boolean canGet(String auth, int teamId) {
        return admin(auth) || teamMember(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canGetAll(String auth) {
        return admin(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canRemove(String auth, int teamId) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and team members (including analysts) are granted permission.
     */
    @Override
    public boolean canGetMembers(String auth, int teamId) {
        return admin(auth) || teamMember(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and team analysts are granted permission.
     */
    @Override
    public boolean canAddMember(String auth, int teamId) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and team analysts are granted permission.
     */
    @Override
    public boolean canRemoveMember(String auth, int teamId) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and team members (including analysts) are granted permission.
     */
    @Override
    public boolean canGetProjects(String auth, int teamId) {
        return admin(auth) || teamMember(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and analysts (assigned to the project <b>and</b> the team) are granted permission.
     */
    @Override
    public boolean canAddProject(String auth, int teamId, String projectName) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and analysts (assigned to the project <b>and</b> the team) are granted permission.
     */
    @Override
    public boolean canRemoveProject(String auth, int teamId, String projectName) {
        return admin(auth) || thisTeamLeader(auth, teamId);
    }

    private boolean user(String auth) {
        return authHandler.hasPermission(auth, true);
    }

    private boolean teamMember(String auth, int teamId) {
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);
        List<PersistentUser> teamMembers = teamDAO.getAllMembersById(teamId);

        return user(auth) && teamMembers.contains(user);
    }

    private boolean admin(String auth) {
        return authHandler.hasPermission(auth, true, true);
    }

    private boolean thisTeamLeader(String auth, int teamId) {
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);
        PersistentTeam team = teamDAO.getById(teamId);

        return team.getLeaderId() == user.getId();
    }
}
