package be.ugent.vopro1.interactor.team;

import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.util.List;
import java.util.Optional;

/**
 * An object to interact with the database, concerning TeamEntities.
 * <p>
 * A TeamInteractor serves as a handle to the database, many operations such as
 * searching, adding, removing and editing database elements are supported
 * through the use of an {@link be.ugent.vopro1.persistence.TeamDAO}.
 * <p>
 * Teams retrieved from the database are formatted as PersistentObjects, the
 * Interactor uses a Converter to translate these objects to TeamEntities.
 */
public interface TeamInteractor {

    /**
     * Adds a Team to the database.
     *
     * @param request the Team to add to the database
     * @return the added team
     * @see Team
     */
    Team addTeam(Team request);

    /**
     * Edits a Team from the database by giving the 'updated' Team
     * the old team's id.
     *
     * @param id the team's id to edit
     * @param updated the edited team
     * @return the new team with id 'id'
     * @see Team
     */
    Team editTeam(int id, Team updated);

    /**
     * Retrieves a Team from the database.
     *
     * @param id the id of the team you wish to retrieve
     * @return the retrieved team
     * @see Team
     */
    Team getTeam(int id);

    /**
     * Retrieves a Team from the database.
     *
     * @param name the name of the team you wish to retrieve
     * @return the retrieved team
     * @see Team
     */
    Team getTeam(String name);

    /**
     * Retrieves all TeamEntities from the database.
     *
     * @return the TeamEntities
     * @see Team
     */
    List<Team> getAllTeams();

    /**
     * Removes a team from the database.
     *
     * @param id the id of the team you wish to remove
     * @see Team
     */
    void removeTeam(int id);

    /**
     * Retrieve the identifier of a team with given name
     *
     * @param teamName Name of the team to retrieve identifier of
     * @return Identifier of the team if it exists
     */
    Optional<Integer> getId(String teamName);

    /**
     * Retrieve all {@link User} objects that belong to the team with id
     * 'id'.
     *
     * @param id the id of the team you wish to retrieve the team members from
     * @param analystOnly Return only users who are analysts for this team
     * @return the UserEntities in team with id 'id'
     * @see User
     */
    List<User> getTeamMembers(int id, boolean analystOnly);

    /**
     * Adds a TeamMember to the database.
     *
     * @param teamId the id of the team to which the teamMember belongs
     * @param userId the id of the user
     */
    void addTeamMember(int teamId, int userId);

    /**
     * Removes a teamMember from the database.
     *
     * @param teamId the id of the team to which the TeamMember belongs
     * @param userId the id of the user
     */
    void removeTeamMember(int teamId, int userId);

    /**
     * Retrieves all the projects this team is working on
     *
     * @param id the id of the team you wish to retrieve the projects from
     * @return all this team's projects
     * @see EntityProject
     */
    List<EntityProject> getProjects(int id);

    /**
     * Add a TeamProject to the database.
     *
     * @param teamId the id of the team
     * @param projectName the name of the project you wish to add to this team
     */
    void addTeamProject(int teamId, String projectName);

    /**
     * Removes a TeamProject from the database.
     *
     * @param teamId the id of the team
     * @param projectName the name of the project you wish to remove from this
     * team
     */
    void removeTeamProject(int teamId, String projectName);
}
