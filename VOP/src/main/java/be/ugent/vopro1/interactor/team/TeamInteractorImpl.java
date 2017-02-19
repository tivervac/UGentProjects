package be.ugent.vopro1.interactor.team;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.util.LocalConstants;
import be.ugent.vopro1.util.error.ErrorMessages;
import be.ugent.vopro1.util.error.RequirementNotMetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link TeamInteractor} interface.
 *
 * @see TeamInteractor
 * @see TeamDAO
 * @see ProjectDAO
 * @see JsonConverter
 */
public class TeamInteractorImpl implements TeamInteractor {

    private TeamDAO teamDAO;
    private ProjectDAO projectDAO;
    private EntityDAO entityDAO;
    private ConverterFacade converter;

    /**
     * Constructs a TeamInteractor.
     *
     * @see TeamDAO
     * @see ProjectDAO
     * @see JsonConverter
     */
    public TeamInteractorImpl() {
        converter = ConverterFactory.getInstance();
        teamDAO = DAOProvider.get("team");
        projectDAO = DAOProvider.get("project");
        entityDAO = DAOProvider.get("entity");
    }

    /**
     * {@inheritDoc}
     *
     * @param request {@inheritDoc}
     * @return {@inheritDoc}
     * @see Team
     * @see JsonConverter
     * @see TeamDAO
     */
    @Override
    public Team addTeam(Team request) {
        checkNotExists(request.getName());

        return convertTeam(teamDAO.save(convertTeam(request)));
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @param updated {@inheritDoc}
     * @return {@inheritDoc}
     * @see Team
     * @see TeamDAO
     * @see JsonConverter
     */
    @Override
    public Team editTeam(int id, Team updated) {
        PersistentTeam team = teamDAO.getById(id);

        PersistentTeam currentPersistent = convertTeam(updated);

        if (! team.getName().equals(currentPersistent.getName())) {
            checkNotExists(currentPersistent.getName());
        }

        currentPersistent.setId(team.getId());

        teamDAO.update(currentPersistent);
        return getTeam(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see Team
     * @see JsonConverter
     * @see TeamDAO
     */
    @Override
    public Team getTeam(int id) {
        checkExists(id);

        return convertTeam(teamDAO.getById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     * @see Team
     * @see TeamDAO
     * @see JsonConverter
     */
    @Override
    public Team getTeam(String name) {
        checkExists(name);

        return convertTeam(teamDAO.getByName(name));
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see Team
     * @see TeamDAO
     * @see #convertTeams(java.util.List)
     */
    @Override
    public List<Team> getAllTeams() {
        return convertTeams(teamDAO.getAll());
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @see TeamDAO
     */
    @Override
    public void removeTeam(int id) {


        teamDAO.deleteById(id);
    }

    @Override
    public Optional<Integer> getId(String teamName) {
        if (teamDAO.exists(teamName)) {
            return Optional.of(teamDAO.getByName(teamName).getId());
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @param analystOnly {@inheritDoc}
     * @return {@inheritDoc}
     * @see User
     * @see TeamDAO
     * @see JsonConverter
     */
    @Override
    public List<User> getTeamMembers(int id, boolean analystOnly) {
        List<PersistentUser> members = teamDAO.getAllMembersById(id);
        if (!analystOnly) {
            return converter.convert(members);
        } else {
            List<PersistentUser> filteredMembers = members.stream().filter(user -> {
                List<PersistentProject> responsibleProjects = teamDAO.getAllProjectsById(id);
                return responsibleProjects.stream().anyMatch(project -> {
                    List<PersistentUser> analystUsers = projectDAO.getAllAnalystsById(project.getId());
                    return analystUsers.contains(user);
                });
            }).collect(Collectors.toCollection(ArrayList::new));

            return converter.convert(filteredMembers);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param userId {@inheritDoc}
     * @see TeamDAO
     */
    @Override
    public void addTeamMember(int teamId, int userId) {
        if (teamDAO.getAllMembersById(teamId)
                .stream()
                .map(PersistentUser::getId)
                .anyMatch((id) -> id == userId)) {
            // User is already a member of this team
            throw new RequirementNotMetException(ErrorMessages.ALREADY_MEMBER);
        }

        teamDAO.addMember(teamId, userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param userId {@inheritDoc}
     * @see TeamDAO
     */
    @Override
    public void removeTeamMember(int teamId, int userId) {
        if (teamDAO.getAllMembersById(teamId)
                .stream()
                .map(PersistentUser::getId)
                .noneMatch((id) -> id == userId)) {
            // User is not a member of this team
            throw new RequirementNotMetException(ErrorMessages.NOT_ALREADY_MEMBER);
        }

        // Remove team member
        teamDAO.deleteMember(teamId, userId);

        // Remove project analyst
        for (PersistentProject project : teamDAO.getAllProjectsById(teamId)) {
            List<PersistentUser> projectAnalysts = projectDAO.getAllAnalystsById(project.getId());
            for (PersistentUser projectAnalyst : projectAnalysts) {
                if (projectAnalyst.getId() == userId) {
                    projectDAO.deleteAnalyst(project.getId(), userId);

                    // Remove entity analyst
                    for (PersistentObject object : entityDAO.getAllForProject(project.getName())) {
                        List<PersistentUser> entityAnalysts = entityDAO.getAllAnalysts(object.getName(), project.getName());
                        for (PersistentUser analyst : entityAnalysts) {
                            if (analyst.getId() == userId) {
                                entityDAO.deleteAnalyst(object.getName(), project.getName(), userId);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @see EntityProject
     * @see TeamDAO
     * @see JsonConverter
     */
    @Override
    public List<EntityProject> getProjects(int id) {
        return converter.convert(teamDAO.getAllProjectsById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @see TeamDAO
     * @see ProjectDAO
     */
    @Override
    public void addTeamProject(int teamId, String projectName) {
        if (teamDAO.getAllProjectsById(teamId)
                .stream()
                .map(PersistentProject::getName)
                .anyMatch((name) -> name.equals(projectName))) {
            // Team is already assigned to the project
            throw new RequirementNotMetException(ErrorMessages.ALREADY_ASSIGNED);
        }

        if (projectDAO.getAllTeamsByName(projectName).size() >= LocalConstants.MAX_TEAMS_PER_PROJECT) {
            throw new RequirementNotMetException(ErrorMessages.OVER_ASSIGNMENT_LIMIT);
        }

        teamDAO.addProject(teamId, projectDAO.getByName(projectName).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param teamId {@inheritDoc}
     * @param projectName {@inheritDoc}
     * @see TeamDAO
     * @see ProjectDAO
     */
    @Override
    public void removeTeamProject(int teamId, String projectName) {
        if (teamDAO.getAllProjectsById(teamId)
                .stream()
                .map(PersistentProject::getName)
                .noneMatch((name) -> name.equals(projectName))) {
            // Team is not already assigned to the project
            throw new RequirementNotMetException(ErrorMessages.NOT_ALREADY_ASSIGNED);
        }

        teamDAO.deleteProject(teamId, projectDAO.getByName(projectName).getId());
    }


    private void checkExists(int teamId) {
        if (!teamDAO.exists(teamId)) {
            // Team does not exist
            throw new RequirementNotMetException(ErrorMessages.TEAM_DOES_NOT_EXIST);
        }
    }

    private void checkExists(String teamName) {
        if (!teamDAO.exists(teamName)) {
            // Team does not exist
            throw new RequirementNotMetException(ErrorMessages.TEAM_DOES_NOT_EXIST);
        }    }

    private void checkNotExists(String teamName) {
        if (teamDAO.exists(teamName)) {
            // Team exists
            throw new RequirementNotMetException(ErrorMessages.TEAM_ALREADY_EXISTS);
        }
    }

    /**
     * Help method to convert a possibly big list of PersistentTeams into a list
     * of TeamEntities.
     *
     * @param persistentTeams database formatted teams
     * @return a List of objects of the correct Team type
     * @see Team
     * @see PersistentTeam
     * @see ConverterFacade
     */
    private List<Team> convertTeams(List<PersistentTeam> persistentTeams) {
        return persistentTeams.stream().map(this::convertTeam).collect(Collectors.toList());
    }

    private PersistentTeam convertTeam(Team team) {
        return converter.convert(team);
    }

    private Team convertTeam(PersistentTeam persistentTeam) {
        Team team = converter.convert(persistentTeam);
        team.setMembers(converter.convert(teamDAO.getAllMembersById(persistentTeam.getId())));

        return team;
    }
}
