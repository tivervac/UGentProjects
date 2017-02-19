package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.EntityDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDAOMock implements EntityDAO {

    @Override
    public PersistentObject save(PersistentObject persistentObject) {
        persistentObject.setId(1);
        return persistentObject;
    }

    @Override
    public PersistentObject getByName(String name, int projectId) {
        return new PersistentObject(1, projectId, name, "actor", "{}");
    }

    @Override
    public PersistentObject getByName(String name, String projectName) {
        return new PersistentObject(1, 1, name, "actor", "{}");
    }

    @Override
    public boolean exists(int id) {
        return false;
    }

    @Override
    public boolean exists(String name, String projectName) {
        return false;
    }

    @Override
    public boolean exists(String name, int projectId) {
        return false;
    }

    @Override
    public PersistentObject getById(int id) {
        return new PersistentObject(1, 1, "name", "actor", "{}");
    }

    @Override
    public void update(PersistentObject persistentObject) {
    }

    @Override
    public void deleteByName(String name, int projectId) {
    }

    @Override
    public void deleteByName(String name, String projectName) {
    }

    @Override
    public void deleteById(int id) {
    }

    @Override
    public List<PersistentObject> getAll() {
        PersistentObject obj = new PersistentObject(1, 1, "name", "actor", "{}");
        List<PersistentObject> objects = new ArrayList<>();
        objects.add(obj);
        return objects;
    }

    @Override
    public List<PersistentObject> getAllForProject(String projectName) {
        PersistentObject obj = new PersistentObject(1, 1, "name", "actor", "{}");
        List<PersistentObject> objects = new ArrayList<>();
        objects.add(obj);
        return objects;
    }

    @Override
    public List<PersistentObject> find(String query) {
        PersistentObject obj1 = new PersistentObject(1, 1, "name", "actor", "{}");
        PersistentObject obj2 = new PersistentObject(2, 1, "other", "actor", "{}");
        List<PersistentObject> objects = new ArrayList<>();
        objects.add(obj1);
        objects.add(obj2);
        return objects.stream().filter(o -> o.getName().equals(query)).collect(Collectors.toList());
    }

    @Override
    public List<PersistentObject> findInProject(String projectName, String query) {
        return find(query);
    }

    @Override
    public List<PersistentUser> getAllAnalysts(String name, String projectName) {
        return null;
    }

    @Override
    public void addAnalyst(String name, String projectName, int userId) {

    }

    @Override
    public void deleteAnalyst(String name, String projectName, int userId) {

    }
}
