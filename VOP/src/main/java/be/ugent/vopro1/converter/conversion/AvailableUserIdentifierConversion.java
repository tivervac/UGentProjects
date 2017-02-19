package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;

import java.util.function.BiFunction;

/**
 * Performs a conversion from a {@link PersistentUser#getId()} and {@link PersistentProject#getName()}
 * to a {@link AvailableUser}
 */
public class AvailableUserIdentifierConversion implements BiFunction<Integer, String, AvailableUser> {
    @Override
    public AvailableUser apply(Integer userId, String projectName) {
        ConverterFacade converter = ConverterFactory.getInstance();
        UserDAO userDAO = DAOProvider.get("user");
        ProjectDAO projectDAO = DAOProvider.get("project");
        int projectId = projectDAO.getByName(projectName).getId();

        return new AvailableUser(converter.convert(userDAO.getById(userId)), userDAO.getWorkhours(userId, projectId));
    }
}
