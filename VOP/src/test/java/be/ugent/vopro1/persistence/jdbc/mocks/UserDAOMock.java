package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAOMock implements UserDAO {

    HashMap<Integer, PersistentUser> persisted = new HashMap<>();
    int lastId = 0;

    @Override
    public PersistentUser save(PersistentUser user) {
        user.setId(++lastId);
        persisted.put(user.getId(), user);
        return user;
    }

    @Override
    public PersistentUser getById(int id) {
        return persisted.get(id);
    }

    @Override
    public PersistentUser getByEmail(String email) {
        return persisted.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public boolean exists(int id) {
        return persisted.containsKey(id);
    }

    @Override
    public boolean exists(String email) {
        return persisted.values().stream().map(PersistentUser::getEmail).anyMatch((userEmail) -> userEmail.equals(email));
    }

    @Override
    public List<PersistentUser> getAll() {
        return persisted.values().stream().collect(Collectors.toList());
    }

    @Override
    public void update(PersistentUser user) {
        persisted.put(user.getId(), user);
    }

    @Override
    public void deleteById(int id) {
        persisted.remove(id);
    }

    @Override
    public void deleteByEmail(String email) {
        persisted.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().ifPresent(u -> deleteById(u.getId()));
    }

    @Override
    public List<PersistentTeam> getAllTeamsById(int id) {
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam().id(1).name("name").build();
        List<PersistentTeam> teams = new ArrayList<>();
        teams.add(team);
        return teams;
    }

    @Override
    public List<PersistentTeam> getAllTeamsByEmail(String email) {
        PersistentTeam team = PersistentTeam.PersistentTeamBuilder.aPersistentTeam().id(1).name("name").build();
        List<PersistentTeam> teams = new ArrayList<>();
        teams.add(team);
        return teams;
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsById(int id) {
        PersistentProject proj = PersistentProject.PersistentProjectBuilder.aPersistentProject().id(1).name("name1").build();
        List<PersistentProject> projects = new ArrayList<>();
        projects.add(proj);
        return projects;
    }

    @Override
    public long getWorkhours(int userId, int projectId) {
        return 0L;
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsByEmail(String email) {
        return null;
    }
}
