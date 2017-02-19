package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.function.Function;

/**
 * Performs a conversion from a {@link PersistentObject#getId()} to a {@link Declaration}
 */
public class DocumentIdentifierConversion implements Function<Integer, Declaration> {
    @Override
    public Declaration apply(Integer integer) {
        ConverterFacade converter = ConverterFactory.getInstance();
        EntityDAO entityDAO = DAOProvider.get("entity");

        return converter.convert(entityDAO.getById(integer));
    }
}
