package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.DocumentNameConversion;
import be.ugent.vopro1.converter.conversion.UserEmailConversion;
import be.ugent.vopro1.model.Assignment;

/**
 * Converts {@link Assignment} into {@link PersistentAssignment}
 */
public class AssignmentConverter extends AbstractBeanConverter<Assignment, PersistentAssignment> {

    private UserEmailConversion userEmailConversion;
    private DocumentNameConversion documentNameConversion;

    /**
     * Creates a new AssignmentConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public AssignmentConverter(ConverterProvider provider) {
        super(provider, Assignment.class);
        userEmailConversion = new UserEmailConversion();
        documentNameConversion = new DocumentNameConversion();
    }

    @Override
    public PersistentAssignment convertBean(Assignment input) {
        PersistentUser persistentUser = userEmailConversion.apply(input.getUser().getUser().getEmail());
        int useCaseId = documentNameConversion.apply(input.getTask().getUsecase());

        return new PersistentAssignment(persistentUser.getId(), useCaseId, input.getInterval());
    }
}
