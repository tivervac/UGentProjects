package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.ProjectDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDAOMock implements ProjectDAO {

    private List<Integer> analysts = new ArrayList<>();
    private List<PersistentTeam> teams = null;
    private List<PersistentProject> projects = new ArrayList<>();
    private int lastId = 0;

    @Override
    public PersistentProject save(PersistentProject persistentProject) {
        projects.add(new PersistentProject(++lastId, persistentProject.getName(), 1));
        return projects.get(lastId-1);
    }

    @Override
    public PersistentProject getByName(String name) {
       return new PersistentProject(1, name, 1);
    }

    @Override
    public PersistentProject getById(int id) {
        return new PersistentProject(id, "name", 1);
    }

    @Override
    public void update(PersistentProject persistentProject) {
        projects.remove(persistentProject.getId()-1);
        projects.add(persistentProject.getId()-1, persistentProject);
    }

    @Override
    public void deleteByName(String name) {
    }

    @Override
    public void deleteById(int id) {
    }

    @Override
    public List<PersistentProject> getAll() {
        PersistentProject proj1 = new PersistentProject(1, "name1", 1);
        PersistentProject proj2 = new PersistentProject(2, "name2", 1);
        List<PersistentProject> projects = new ArrayList<>();
        projects.add(proj1);
        projects.add(proj2);
        return projects;
    }

    @Override
    public boolean exists(int id) {
        return lastId >= id;
    }

    @Override
    public boolean exists(String name) {
        return projects.stream().filter(project -> project.getName().equals(name)).count() >= 1;
    }

    @Override
    public List<PersistentProject> find(String query) {
        PersistentProject proj1 = new PersistentProject(1, "name1", 1);
        PersistentProject proj2 = new PersistentProject(2, "name2", 1);
        List<PersistentProject> projects = new ArrayList<>();
        projects.add(proj1);
        projects.add(proj2);
        return projects.stream().filter(p -> p.getName().equals(query)).collect(Collectors.toList());
    }

    @Override
    public List<PersistentTeam> getAllTeamsById(int id) {
        if (teams != null) { return teams; }
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam().id(id).name("team1").build();
        List<PersistentTeam> teams = new ArrayList<>();
        teams.add(team);
        return teams;
    }

    @Override
    public List<PersistentTeam> getAllTeamsByName(String name) {
        if (teams != null) { return teams; }
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam().id(1).name(name).build();
        List<PersistentTeam> teams = new ArrayList<>();
        teams.add(team);
        return teams;
    }

    @Override
    public List<PersistentUser> getAllAnalystsById(int id) {
        PersistentUser user = PersistentUser.PersistentUserBuilder.aPersistentUser().id(1).email("me@example.com").build();
        List<PersistentUser> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    @Override
    public List<PersistentUser> getAllAnalystsByName(String name) {
        PersistentUser user = PersistentUser.PersistentUserBuilder.aPersistentUser().id(1).email("me@example.com").build();
        List<PersistentUser> users = new ArrayList<>();
        users.add(user);
        return users;
    }

    //TODO: test workload
    @Override
    public void addAnalyst(int projectId, int userId, long workload) {
        analysts.add(userId);
    }

    //TODO: test
    @Override
    public void editAnalyst(int projectId, int userId, long workload) {

    }

    @Override
    public void deleteAnalyst(int projectId, int userId) {
        analysts.remove(new Integer(userId));
    }

    // For tests
    public List<Integer> getAnalysts() {
        return analysts;
    }

    public void setTeams(List<PersistentTeam> teams) {
        this.teams = teams;
    }
}
