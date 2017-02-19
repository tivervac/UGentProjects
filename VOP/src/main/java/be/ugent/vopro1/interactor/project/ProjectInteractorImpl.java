package be.ugent.vopro1.interactor.project;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.util.LocalConstants;
import be.ugent.vopro1.util.error.ErrorMessages;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.workspace.ProjectException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link ProjectInteractor} interface.
 *
 * @see CustomWorkspace
 * @see ProjectDAO
 * @see ConverterFacade
 * @see EntityProject
 */
public class ProjectInteractorImpl implements ProjectInteractor {
    
    private CustomWorkspace workspace;
    private ProjectDAO projectDAO;
    private TeamDAO teamDAO;
    private EntityDAO entityDAO;
    private UserDAO userDAO;
    private ConverterFacade converter;

    /**
     * Constructs a ProjectInteractor.
     *
     * @see CustomWorkspace
     * @see ProjectDAO
     * @see EntityProject
     */
    public ProjectInteractorImpl() {
        converter = ConverterFactory.getInstance();
        workspace = WorkspaceFactory.getInstance();
        projectDAO = DAOProvider.get("project");
        entityDAO = DAOProvider.get("entity");
        teamDAO = DAOProvider.get("team");
        userDAO = DAOProvider.get("user");
    }

    /**
     * {@inheritDoc}
     *
     * @param project {@inheritDoc}
     * @return {@inheritDoc}
     * @see CustomWorkspace
     * @see ProjectDAO
     * @see EntityProject
     */
    public EntityProject addProject(EntityProject project) {
        checkNotExists(project.getName());

        EntityProject result = converter.convert(projectDAO.save(converter.convert(project)));
        try {
            workspace.createProject(project.getName());
        } catch (ProjectException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param oldProject {@inheritDoc}
     * @param newProject {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     * @see EntityProject
     */
    @Override
    public EntityProject editProject(EntityProject oldProject, EntityProject newProject) {
        PersistentProject oldProj = projectDAO.getByName(oldProject.getName());
        PersistentProject newProj = converter.convert(newProject);
        // Ensure the new project has the correct identifier
        newProj.setId(oldProj.getId());

        if (!oldProject.getName().equals(newProject.getName())) {
            checkNotExists(newProject.getName());
        }

        projectDAO.update(newProj);

        try {
            workspace.renameProject(oldProject.getName(), newProject.getName());
        } catch (LookupException e) {
            // We really should let these be thrown to the adapter layer
            e.printStackTrace();
        }

        return getProject(newProject);
    }

    /**
     * {@inheritDoc}
     *
     * @param project {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     * @see EntityProject
     */
    @Override
    public EntityProject getProject(EntityProject project) {
        checkExists(project.getName());

        return converter.convert(projectDAO.getByName(project.getName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param filterTeamAssignable {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     * @see EntityProject
     * @see LocalConstants#MAX_TEAMS_PER_PROJECT
     */
    @Override
    public List<EntityProject> getAllProjects(boolean filterTeamAssignable) {
        List<PersistentProject> projects = projectDAO.getAll();

        if (filterTeamAssignable) {
            // Only return projects that have not yet reached the assignment cap
            projects = projects.stream()
                    .filter((project) -> projectDAO.getAllTeamsById(project.getId()).size() < LocalConstants.MAX_TEAMS_PER_PROJECT)
                    .collect(Collectors.toList());
        }

        return converter.convert(projects);
    }

    /**
     * {@inheritDoc}
     *
     * @param query {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     * @see EntityProject
     * @see
     * <a href="http://www.postgresql.org/docs/8.3/static/functions-matching.html#FUNCTIONS-POSIX-REGEXP">Postgresql
     * regex matching</a>
     */
    @Override
    public List<EntityProject> findProjects(String query) {
        return converter.convert(projectDAO.find(query));
    }

    /**
     * {@inheritDoc}
     *
     * @param project {@inheritDoc}
     * @see CustomWorkspace
     * @see ProjectDAO
     * @see EntityProject
     */
    @Override
    public Void removeProject(EntityProject project) {
        checkExists(project.getName());

        projectDAO.deleteByName(project.getName());

        try {
            workspace.remove(workspace.getProject(project.getName()));
        } catch (LookupException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public List<Team> getTeams(int id) {
        return converter.convert(projectDAO.getAllTeamsById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public List<Team> getTeams(String projectName) {
        checkExists(projectName);

        return getTeams(projectDAO.getByName(projectName).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public List<AvailableUser> getAnalysts(String name) {
        checkExists(name);

        List<AvailableUser> availableUsers = new ArrayList<>();
        int projectId = projectDAO.getByName(name).getId();
        List<PersistentUser> users = projectDAO.getAllAnalystsById(projectId);
        for (PersistentUser user : users) {
            availableUsers.add(
                    new AvailableUser(converter.convert(user), userDAO.getWorkhours(user.getId(), projectId)));
        }

        return availableUsers;
    }
    
    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public List<User> getEligibleAnalysts(String projectName) {
        checkExists(projectName);

        Set<PersistentUser> resultSet = new HashSet<>();
        
        projectDAO.getAllTeamsByName(projectName).stream()
                .forEach(team -> {
                    teamDAO.getAllMembersById(team.getId()).stream()
                            .forEach(resultSet::add);
                });
        
        return converter.convert(new ArrayList<>(resultSet));
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param userId {@inheritDoc}
     * @param work {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public void addAnalyst(String name, int userId, long work) {
        if (projectDAO.getAllTeamsByName(name).stream()
                .map(PersistentTeam::getId)
                .filter((teamId) ->
                                teamDAO.getAllMembersById(teamId)
                                        .stream()
                                        .map(PersistentUser::getId)
                                        .anyMatch((memberId) -> memberId == userId)
                )
                .count() < 1) {
            throw new RequirementNotMetException(ErrorMessages.TEAM_MEMBER_REQUIRED);
        }

        if (projectDAO.getAllAnalystsByName(name)
                .stream()
                .map(PersistentUser::getId)
                .anyMatch((analystId) -> analystId == userId)) {
            // User is already an analyst for this project
            throw new RequirementNotMetException(ErrorMessages.ALREADY_PROJECT_ANALYST);
        }

        projectDAO.addAnalyst(projectDAO.getByName(name).getId(), userId, work);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param userId {@inheritDoc}
     * @param workload {@inheritDoc}
     */
    @Override
    public void editAnalyst(String name, int userId, long workload) {
        checkExists(name);

        if (projectDAO.getAllAnalystsByName(name)
                .stream()
                .map(PersistentUser::getId)
                .noneMatch((analystId) -> analystId == userId)) {
            throw new RequirementNotMetException(ErrorMessages.NOT_ALREADY_PROJECT_ANALYST);
        }
        projectDAO.editAnalyst(projectDAO.getByName(name).getId(), userId, workload);
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @param userId {@inheritDoc}
     * @see ProjectDAO
     */
    @Override
    public void removeAnalyst(String name, int userId) {
        if (projectDAO.getAllAnalystsByName(name)
                .stream()
                .map(PersistentUser::getId)
                .noneMatch((analystId) -> analystId == userId)) {
            throw new RequirementNotMetException(ErrorMessages.NOT_ALREADY_PROJECT_ANALYST);
        }

        // Remove project analyst
        projectDAO.deleteAnalyst(projectDAO.getByName(name).getId(), userId);

        // Remove entity analyst if required
        for (PersistentObject object : entityDAO.getAllForProject(name)) {
            List<PersistentUser> analysts = entityDAO.getAllAnalysts(object.getName(), name);
            for (PersistentUser analyst : analysts) {
                if (analyst.getId() == userId) {
                    entityDAO.deleteAnalyst(object.getName(), name, userId);
                }
            }
        }
    }

    private void checkExists(String projectName) {
        if (!projectDAO.exists(projectName)) {
            // projectName does not exist
            throw new RequirementNotMetException(ErrorMessages.PROJECT_DOES_NOT_EXIST);
        }
    }

    private void checkNotExists(String projectName) {
        if (projectDAO.exists(projectName)) {
            // Team exists
            throw new RequirementNotMetException(ErrorMessages.PROJECT_ALREADY_EXISTS);
        }
    }
}
