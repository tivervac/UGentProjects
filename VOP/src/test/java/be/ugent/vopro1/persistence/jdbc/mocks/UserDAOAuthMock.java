package be.ugent.vopro1.persistence.jdbc.mocks;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.NonTransientDataAccessException;

import java.util.Arrays;
import java.util.List;

public class UserDAOAuthMock implements UserDAO {
    @Override
    public PersistentUser save(PersistentUser user) {
        return null;
    }

    @Override
    public PersistentUser getById(int id) {
        return null;
    }

    @Override
    public PersistentUser getByEmail(String email) {
        PersistentUser.PersistentUserBuilder builder = PersistentUser.PersistentUserBuilder.aPersistentUser()
                .firstName("first")
                .lastName("last")
                .unhashedPassword("lolcode")
                .id(1)
                .email(email)
                .but();

        switch (email) {
            case "admin@example.com":
                return builder.admin(true).build();
            case "user@example.com":
                return builder.admin(false).build();
            default:
                throw new DataRetrievalFailureException("User does not exist");
        }
    }

    @Override
    public boolean exists(int id) {
        return true;
    }

    @Override
    public boolean exists(String email) {
        String[] possibilities = {
                "admin@example.com",
                "user@example.com"
        };

        return Arrays.stream(possibilities).anyMatch(str -> str.equals(email));
    }

    @Override
    public List<PersistentUser> getAll() {
        return null;
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
        return null;
    }

    @Override
    public List<PersistentTeam> getAllTeamsByEmail(String email) {
        return null;
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsById(int id) {
        return null;
    }

    @Override
    public long getWorkhours(int userId, int projectId) {
        return 0;
    }

    @Override
    public List<PersistentProject> getAllAnalystProjectsByEmail(String email) {
        return null;
    }
}
