package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.function.Function;

/**
 * Performs a conversion from a {@link Declaration#name()} and {@link Declaration#project()} to a {@link Integer}
 */
public class DocumentNameConversion implements Function<Declaration, Integer> {
    @Override
    public Integer apply(Declaration declaration) {
        EntityDAO entityDAO = DAOProvider.get("entity");
        if (declaration == null) {
            return -1;
        }

        return entityDAO.getByName(declaration.name(), declaration.project().getName()).getId();
    }
}
