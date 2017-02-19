package be.ugent.vopro1.interactor.user;

import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandler;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;

import java.util.List;

/**
 * An implementation of the {@link UserPermission} interface with a few sensible
 * default permission settings.
 */
public class UserPermissionImpl implements UserPermission {

    private UserDAO userDAO;
    private TeamDAO teamDAO;
    private HeaderAuthenticationHandler authHandler;

    /**
     * Creates a new UserPermissionImpl and initializes some
     * services that it depends on.
     */
    public UserPermissionImpl() {
        this.userDAO = DAOProvider.get("user");
        this.teamDAO = DAOProvider.get("team");
        this.authHandler = AuthenticationFactory.getInstance("header");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Everyone is granted permission. A possible alternative would be allow
     * only admins to add users.
     */
    @Override
    public boolean canAdd(String auth) {
        // Everyone can register a new user
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission. Other users can edit their own account.
     */
    @Override
    public boolean canEdit(String auth, int id) {
        return selfOrAdmin(auth, id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission. Other users can view their own account.
     */
    @Override
    public boolean canGet(String auth, int id) {
        return selfOrAdmin(auth, id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission.
     */
    @Override
    public boolean canUpgrade(String auth) {
        return admin(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Users are granted permission.
     */
    @Override
    public boolean canGetSelf(String auth) {
        return user(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins and analysts are granted permission.
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
    public boolean canRemove(String auth) {
        return admin(auth);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission. Other users can view their own teams.
     */
    @Override
    public boolean canGetTeams(String auth, int id) {
        return selfOrAdmin(auth, id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Admins are granted permission. Other users can view their own data.
     */
    @Override
    public boolean canGetAnalystList(String auth, int userId) {
        return selfOrAdmin(auth, userId);
    }

    private boolean user(String auth) {
        return authHandler.hasPermission(auth, true);
    }
    
    private boolean aTeamLeader(String auth) {
        if (!user(auth)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(authHandler.getEmail(auth));
        List<PersistentTeam> teams = teamDAO.getAll();

        return teams.stream().anyMatch(team -> team.getLeaderId() == user.getId());
    }

    private boolean admin(String auth) {
        return authHandler.hasPermission(auth, true, true);
    }

    private boolean self(String auth, int id) {
        String email = authHandler.getEmail(auth);

        if (!userDAO.exists(email)) {
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);
        return user(auth) && user.getId() == id;
    }

    private boolean selfOrAdmin(String auth, int id) {
        return self(auth, id) || admin(auth);
    }

}
