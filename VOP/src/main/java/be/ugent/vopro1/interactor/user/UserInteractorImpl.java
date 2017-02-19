package be.ugent.vopro1.interactor.user;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.util.error.ErrorMessages;
import be.ugent.vopro1.util.error.RequirementNotMetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link UserInteractor} interface.
 *
 * @see ConverterFacade
 * @see UserDAO
 */
public class UserInteractorImpl implements UserInteractor {

    private UserDAO userDAO;
    private ProjectDAO projectDAO;
    private TeamDAO teamDAO;
    private ConverterFacade converter;

    /**
     * Constructs a ProjectInteractor.
     *
     * @see ConverterFacade
     * @see UserDAO
     */
    public UserInteractorImpl() {
        converter = ConverterFactory.getInstance();
        userDAO = DAOProvider.get("user");
        projectDAO = DAOProvider.get("project");
        teamDAO = DAOProvider.get("team");
    }

    /**
     * {@inheritDoc}
     *
     * @param entity {@inheritDoc}
     * @return {@inheritDoc}
     * @see UserDAO
     * @see User
     * @see ConverterFacade
     */
    public User addUser(User entity) {
        checkNotExists(entity.getEmail());

        return converter.convert(userDAO.save(converter.convert(entity)));
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @param current {@inheritDoc}
     * @return {@inheritDoc}
     * @see User
     * @see PersistentUser
     * @see UserDAO
     * @see ConverterFacade
     */
    @Override
    public User editUser(int id, User current) {
        checkExists(id);

        PersistentUser user = userDAO.getById(id);
        current.setAdmin(user.isAdmin());       // Make sure not to edit admin status

        PersistentUser currentPersistent = converter.convert(current);
        currentPersistent.setId(user.getId());

        if (!user.getEmail().equals(currentPersistent.getEmail())) {
            checkNotExists(currentPersistent.getEmail());
        }

        userDAO.update(currentPersistent);

        return getUser(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @param current {@inheritDoc}
     * @return {@inheritDoc}
     * @see User
     * @see PersistentUser
     * @see UserDAO
     * @see ConverterFacade
     */
    @Override
    public User upgradeUser(int id, User current) {
        checkExists(id);

        PersistentUser user = userDAO.getById(id);

        PersistentUser currentPersistent = converter.convert(current);
        currentPersistent.setId(user.getId());

        if (!user.getEmail().equals(currentPersistent.getEmail())) {
            checkNotExists(currentPersistent.getEmail());
        }

        userDAO.update(currentPersistent);

        return getUser(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see User
     * @see ConverterFacade
     * @see UserDAO
     */
    @Override
    public User getUser(int id) {
        checkExists(id);

        return converter.convert(userDAO.getById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @return {@inheritDoc}
     * @see User
     * @see ConverterFacade
     * @see UserDAO
     */
    @Override
    public User getUser(String email) {
        checkExists(email);

        return converter.convert(userDAO.getByEmail(email));
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see User
     * @see UserDAO
     * @see #convertUsers(java.util.List)
     */
    @Override
    public List<User> getAllUsers() {
        return convertUsers(userDAO.getAll());
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see UserDAO
     */
    @Override
    public void removeUser(int id) {
        checkExists(id);

        userDAO.deleteById(id);
    }

    @Override
    public Optional<Integer> getId(String email) {
        if (userDAO.exists(email)) {
            return Optional.of(userDAO.getByEmail(email).getId());
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see Team
     * @see UserDAO
     * @see ConverterFacade
     */
    @Override
    public List<Team> getTeams(int id, boolean analystOnly) {
        List<Team> teams = converter.convert(userDAO.getAllTeamsById(id));

        if (!analystOnly) {
            return teams;
        } else {
            return teams.stream().filter(team -> {
                List<PersistentProject> responsibleProjects = teamDAO.getAllProjectsByName(team.getName());
                return responsibleProjects.stream().anyMatch(project -> {
                    List<PersistentUser> analystUsers = projectDAO.getAllAnalystsById(project.getId());
                    return analystUsers.stream().mapToInt(PersistentUser::getId).anyMatch(x -> id == x);
                });
            }).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userEmail {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public long getWorkhours(String userEmail, String projectName) {
        int userId = getId(userEmail).get();
        int projectId = projectDAO.getByName(projectName).getId();
        return userDAO.getWorkhours(userId, projectId);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see EntityProject
     * @see UserDAO
     * @see ConverterFacade
     */
    @Override
    public List<EntityProject> getAnalystProjects(int id) {
        return converter.convert(userDAO.getAllAnalystProjectsById(id));
    }

    private void checkExists(int userId) {
        if (!userDAO.exists(userId)) {
            // User does not exist
            throw new RequirementNotMetException(ErrorMessages.USER_DOES_NOT_EXIST);
        }
    }

    private void checkExists(String email) {
        if (!userDAO.exists(email)) {
            // Entity name does not exist within the project
            throw new RequirementNotMetException(ErrorMessages.USER_DOES_NOT_EXIST);
        }
    }

    private void checkNotExists(int userId) {
        if (userDAO.exists(userId)) {
            // User does not exist
            throw new RequirementNotMetException(ErrorMessages.USER_ALREADY_EXISTS);
        }
    }

    private void checkNotExists(String email) {
        if (userDAO.exists(email)) {
            // Entity name exists within the project
            throw new RequirementNotMetException(ErrorMessages.USER_ALREADY_EXISTS);
        }
    }

    /**
     * Help method to convert a possibly big list of PersistentUsers into a list
     * of UserEntities.
     *
     * @param persistentUsers database formatted users
     * @return a List of objects of the correct User type
     * @see User
     * @see PersistentUser
     * @see ConverterFacade
     */
    private List<User> convertUsers(List<PersistentUser> persistentUsers) {
        return converter.convert(persistentUsers);
    }
}
