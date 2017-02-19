package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.AvailableUserIdentifierConversion;
import be.ugent.vopro1.converter.conversion.TaskIdentifierConversion;
import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.Task;

/**
 * Converts {@link PersistentAssignment} into {@link Assignment}
 */
public class PersistentAssignmentConverter extends AbstractBeanConverter<PersistentAssignment, Assignment> {

    private AvailableUserIdentifierConversion availableUserIdentifierConversion;
    private TaskIdentifierConversion taskIdentifierConversion;

    /**
     * Creates a new PersistentAssignmentConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentAssignmentConverter(ConverterProvider provider) {
        super(provider, PersistentAssignment.class);
        availableUserIdentifierConversion = new AvailableUserIdentifierConversion();
        taskIdentifierConversion = new TaskIdentifierConversion();
    }

    @Override
    public Assignment convertBean(PersistentAssignment input) {
        Task task = taskIdentifierConversion.apply(input.getTaskId());
        AvailableUser availableUser = availableUserIdentifierConversion.apply(input.getUserId(),
                task.getUsecase().project().getName());

        return new Assignment(
                availableUser,
                input.getInterval(),
                task);
    }
}
