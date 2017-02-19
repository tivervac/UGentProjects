package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;

import java.util.function.Function;

/**
 * Performs a conversion from a {@link User#getEmail()} to a {@link PersistentUser}
 */
public class UserEmailConversion implements Function<String, PersistentUser> {
    @Override
    public PersistentUser apply(String email) {
        UserDAO userDAO = DAOProvider.get("user");

        return userDAO.getByEmail(email);
    }
}
