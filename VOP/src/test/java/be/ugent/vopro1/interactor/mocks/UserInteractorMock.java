package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInteractorMock implements UserInteractor {

    private RuntimeException throwingRun = null;

    @Override
    public User addUser(User request) {
        maybeThrowRuntime();
        return request;
    }

    @Override
    public User editUser(int id, User updated) {
        maybeThrowRuntime();
        return updated;
    }

    @Override
    public User upgradeUser(int id, User updated) {
        return editUser(id, updated);
    }

    @Override
    public User getUser(int id) {
        maybeThrowRuntime();
        return User.UserBuilder.aUser().email("me@example.com").build();
    }

    @Override
    public User getUser(String email) {
        maybeThrowRuntime();
        return User.UserBuilder.aUser().email(email).build();
    }

    @Override
    public List<User> getAllUsers() {
        maybeThrowRuntime();
        List<User> entities = new ArrayList<>();
        entities.add(User.UserBuilder.aUser().email("1@ex.com").build());
        entities.add(User.UserBuilder.aUser().email("2@ex.com").build());
        return entities;
    }

    @Override
    public void removeUser(int id) {
        maybeThrowRuntime();
    }

    @Override
    public Optional<Integer> getId(String email) {
        return Optional.of(1);
    }

    @Override
    public long getWorkhours(String userEmail, String projectName) {
        return 0L;
    }

    @Override
    public List<Team> getTeams(int id, boolean analystOnly) {
        maybeThrowRuntime();
        List<Team> entities = new ArrayList<>();
        entities.add(Team.TeamBuilder.aTeam().name("name1").build());
        entities.add(Team.TeamBuilder.aTeam().name("name2").build());
        return entities;
    }

    @Override
    public List<EntityProject> getAnalystProjects(int id) {
        maybeThrowRuntime();
        List<EntityProject> entities = new ArrayList<>();
        entities.add(new EntityProject("name1"));
        entities.add(new EntityProject("name2"));
        return entities;
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
