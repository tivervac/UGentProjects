package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamInteractorMock implements TeamInteractor {

    private RuntimeException throwingRun = null;

    @Override
    public Team addTeam(Team request) {
        maybeThrowRuntime();
        return request;
    }

    @Override
    public Team editTeam(int id, Team updated) {
        maybeThrowRuntime();
        return updated;
    }

    @Override
    public Team getTeam(int id) {
        maybeThrowRuntime();
        return Team.TeamBuilder.aTeam().name("name").build();
    }

    @Override
    public Team getTeam(String name) {
        maybeThrowRuntime();
        return Team.TeamBuilder.aTeam().name(name).build();
    }

    @Override
    public List<Team> getAllTeams() {
        maybeThrowRuntime();
        List<Team> entities = new ArrayList<>();
        entities.add(Team.TeamBuilder.aTeam().name("name1").build());
        entities.add(Team.TeamBuilder.aTeam().name("name2").build());
        return entities;
    }

    @Override
    public void removeTeam(int id) {
        maybeThrowRuntime();
    }

    @Override
    public Optional<Integer> getId(String teamName) {
        return Optional.of(1);
    }

    @Override
    public List<User> getTeamMembers(int id, boolean analystOnly) {
        maybeThrowRuntime();
        List<User> entities = new ArrayList<>();
        entities.add(User.UserBuilder.aUser().email("1@me.com").build());
        entities.add(User.UserBuilder.aUser().email("2@me.com").build());
        return entities;
    }

    @Override
    public void addTeamMember(int teamId, int userId) {
        maybeThrowRuntime();
    }

    @Override
    public void removeTeamMember(int teamId, int userId) {
        maybeThrowRuntime();
    }

    @Override
    public List<EntityProject> getProjects(int id) {
        maybeThrowRuntime();
        List<EntityProject> entities = new ArrayList<>();
        entities.add(new EntityProject("proj1"));
        entities.add(new EntityProject("proj2"));
        return entities;
    }

    @Override
    public void addTeamProject(int teamId, String projectName) {
        maybeThrowRuntime();
    }

    @Override
    public void removeTeamProject(int teamId, String projectName) {
        maybeThrowRuntime();
    }

    private void maybeThrowRuntime() {
        if (throwingRun != null) {
            throw throwingRun;
        }
    }

    /**
     * Set an exception that the next call to the mock interactor should throw
     * @param ex Exception to throw next
     */
    public void setThrowing(RuntimeException ex) {
        this.throwingRun = ex;
    }
}
