package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;

import java.util.function.Function;

/**
 * Performs a conversion from a {@link PersistentUser#getId()} to a {@link User}
 */
public class UserIdentifierConversion implements Function<Integer, User> {
    @Override
    public User apply(Integer identifier) {
        ConverterFacade converter = ConverterFactory.getInstance();
        UserDAO userDAO = DAOProvider.get("user");

        return converter.convert(userDAO.getById(identifier));
    }
}
