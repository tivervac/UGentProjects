package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class UserDAOProjectMock implements UserDAO {

    private UserDAOMock mock = new UserDAOMock();

    @Override
    public PersistentUser save(PersistentUser user) {
        return user;
    }

    @Override
    public PersistentUser getById(int id) {
        return PersistentUser.PersistentUserBuilder.aPersistentUser().email("test@test.com").id(id).build();
    }

    @Override
    public PersistentUser getByEmail(String email) {
        return PersistentUser.PersistentUserBuilder.aPersistentUser().email(email).id(1).build();
    }

    @Override
    public boolean exists(int id) {
        return true;
    }

    @Override
    public boolean exists(String email) {
        return true;
    }

    @Override
    public List<PersistentUser> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void update(PersistentUser user) {
    }

    @Override
    public void deleteById(int id) {
    }

    @Override
    public void deleteByEmail(String email) {
    }

    @Override
    public List<PersistentTeam> getAllTeamsById(int id) {
        return mock.getAllTeamsById(id);
    }

    @Override
    public List<PersistentTeam> getAllTeamsByEmail(String email) {
        return mock.getAllTeamsByEmail(email);
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsById(int id) {
        return mock.getAllAnalystProjectsById(id);
    }

    @Override
    public long getWorkhours(int userId, int projectId) {
        return 0L;
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsByEmail(String email) {
        return mock.getAllAnalystProjectsByEmail(email);
    }
}
