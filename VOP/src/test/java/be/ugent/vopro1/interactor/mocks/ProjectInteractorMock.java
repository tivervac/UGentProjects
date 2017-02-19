package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectInteractorMock implements ProjectInteractor {

    private IOException throwing = null;
    private RuntimeException throwingRun = null;

    @Override
    public EntityProject addProject(EntityProject project) {
        maybeThrowRuntime();
        return new EntityProject(project.getName());
    }

    @Override
    public EntityProject editProject(EntityProject oldProject, EntityProject newProject) {
        maybeThrowRuntime();
        return new EntityProject(newProject.getName());
    }

    @Override
    public EntityProject getProject(EntityProject project) {
        maybeThrowRuntime();
        return new EntityProject(project.getName());
    }

    @Override
    public List<EntityProject> getAllProjects(boolean teamAssignable) {
        maybeThrowRuntime();
        return new ArrayList<>(Arrays.asList(new EntityProject("project1"), new EntityProject("project2")));
    }

    @Override
    public List<EntityProject> findProjects(String query)  {
        maybeThrowRuntime();
        return new ArrayList<>(Arrays.asList(new EntityProject("project")));
    }

    @Override
    public Void removeProject(EntityProject project) {
        maybeThrowRuntime();
        return null;
    }

    @Override
    public List<Team> getTeams(int id) {
        Team.TeamBuilder builder = Team.TeamBuilder.aTeam();
        maybeThrowRuntime();
        return new ArrayList<>(Arrays.asList(builder.but().name("team1").build(),
                builder.but().name("team2").build()));
    }

    @Override
    public List<Team> getTeams(String projectName) {
        return getTeams(1);
    }

    @Override
    public List<AvailableUser> getAnalysts(String projectName) {
        User.UserBuilder builder = User.UserBuilder.aUser()
                .admin(true)
                .firstName("John")
                .lastName("Doe")
                .password("lolcode");

        maybeThrowRuntime();
        return new ArrayList<>(Arrays.asList(new AvailableUser(builder.but().email("john1@doe.com").build(), 1000),
                                             new AvailableUser(builder.but().email("john2@doe.com").build(), 2999)));

    }
    
    @Override
    public List<User> getEligibleAnalysts(String projectName) {
        User.UserBuilder builder = User.UserBuilder.aUser()
                .admin(false)
                .firstName("Jane")
                .lastName("Doe")
                .password("lolcode");
        
        maybeThrowRuntime();
        return new ArrayList<>(Arrays.asList(builder.but().email("john1@doe.com").build(),
                                             builder.but().email("john2@doe.com").build()));
    }

    @Override
    public void addAnalyst(String projectName, int userId, long workload) {
        maybeThrowRuntime();
    }

    //TODO: test
    @Override
    public void editAnalyst(String projectName, int userId, long workload) {

    }

    @Override
    public void removeAnalyst(String projectName, int userId) {
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
        this.throwing = null;
        this.throwingRun = ex;
    }
}
