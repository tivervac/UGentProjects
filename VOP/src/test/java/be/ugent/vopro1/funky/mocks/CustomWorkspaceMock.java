package be.ugent.vopro1.funky.mocks;

import be.ugent.vopro1.funky.CustomWorkspace;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.workspace.LanguageRepository;
import org.aikodi.chameleon.workspace.ProjectException;

import java.util.*;

public class CustomWorkspaceMock extends CustomWorkspace {

    private Set<String> projects = new HashSet<>();
    private HashMap<String, List<Integer>> entities = new HashMap<>();

    public CustomWorkspaceMock(LanguageRepository repo) {
        super(repo);
    }

    @Override
    public void createProject(String projectName) throws ProjectException {
        if (projects == null) return;

        System.err.println("Project " + projectName + " created");
        if (!projects.add(projectName)) {
            throw new ProjectException("Project with this name already exists");
        }
    }

    @Override
    public void renameProject(String oldName, String newName) {

    }

    @Override
    public void addEntityToProject(int entityId, String projectName) throws LookupException {
        if (!projects.contains(projectName)) {
            throw new LookupException("Project with this name does not exist");
        } else {
            entities.put(projectName, new ArrayList<>());
            entities.get(projectName).add(entityId);
        }
    }

    @Override
    public void removeEntityFromProject(int entityId, String projectName) throws LookupException {
        if (!projects.contains(projectName)) {
            throw new LookupException("Project with this name does not exist");
        } else {
            entities.get(projectName).remove(entityId);
        }
    }

    // For tests
    public Set<String> getProjects() {
        return projects;
    }

    // For tests
    public HashMap<String, List<Integer>> getEntities() {
        return entities;
    }
}
