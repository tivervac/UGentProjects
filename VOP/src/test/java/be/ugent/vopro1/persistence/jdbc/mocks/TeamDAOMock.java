package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.TeamDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TeamDAOMock implements TeamDAO {

    private HashMap<Integer, PersistentTeam> persistentTeams = new HashMap<>();
    private HashMap<Integer, List<Integer>> persistentMembers = new HashMap<>();
    private HashMap<Integer, List<Integer>> persistentProjects = new HashMap<>();
    private int lastTeamId = 0;

    @Override
    public PersistentTeam save(PersistentTeam team) {
        team.setId(++lastTeamId);
        persistentTeams.put(team.getId(), team);
        return team;
    }

    @Override
    public PersistentTeam getById(int id) {
        return persistentTeams.get(id);
    }

    @Override
    public PersistentTeam getByName(String name) {
        return persistentTeams.values().stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public boolean exists(int id) {
        return persistentTeams.containsKey(id);
    }

    @Override
    public boolean exists(String name) {
        return persistentTeams.values().stream().anyMatch(team -> team.getName().equals(name));
    }

    @Override
    public List<PersistentTeam> getAll() {
        return persistentTeams.values().stream().collect(Collectors.toList());
    }

    @Override
    public void update(PersistentTeam team) {
        persistentTeams.put(team.getId(), team);
    }

    @Override
    public void deleteById(int id) {
        persistentTeams.remove(id);
    }

    @Override
    public void deleteByName(String name) {
        persistentTeams.values().stream().filter(t -> t.getName().equals(name)).findFirst().ifPresent(t -> deleteById(t.getId()));
    }

    @Override
    public List<PersistentUser> getAllMembersById(int id) {
        return persistentMembers
                .getOrDefault(id, new ArrayList<>())
                .stream()
                .map(i -> PersistentUser.PersistentUserBuilder.aPersistentUser().id(i).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PersistentUser> getAllMembersByName(String name) {
        PersistentTeam team = getByName(name);
        return getAllMembersById(team.getId());
    }

    @Override
    public void addMember(int teamId, int userId) {
        if (!persistentMembers.containsKey(teamId)) {
            persistentMembers.put(teamId, new ArrayList<>());
        }

        persistentMembers.get(teamId).add(userId);
    }

    @Override
    public void deleteMember(int teamId, int userId) {
        if (persistentMembers.containsKey(teamId)) {
            persistentMembers.get(teamId).remove(new Integer(userId));
        }
    }

    @Override
    public List<PersistentProject> getAllProjectsById(int id) {
        return persistentProjects
                .getOrDefault(id, new ArrayList<>())
                .stream()
                .map(i -> PersistentProject.PersistentProjectBuilder.aPersistentProject().id(i).name("name").build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PersistentProject> getAllProjectsByName(String name) {
        PersistentTeam team = getByName(name);
        return getAllProjectsById(team.getId());
    }

    @Override
    public void addProject(int teamId, int projectId) {
        if (!persistentProjects.containsKey(teamId)) {
            persistentProjects.put(teamId, new ArrayList<>());
        }

        persistentProjects.get(teamId).add(projectId);
    }

    @Override
    public void deleteProject(int teamId, int projectId) {
        if (persistentProjects.containsKey(teamId)) {
            persistentProjects.get(teamId).remove(new Integer(projectId));
        }
    }
}
