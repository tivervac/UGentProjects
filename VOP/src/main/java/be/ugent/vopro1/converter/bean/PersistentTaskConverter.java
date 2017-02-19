package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.DocumentIdentifierConversion;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.model.UsecaseEntity;

/**
 * Converts {@link PersistentTask} into {@link Task}
 */
public class PersistentTaskConverter extends AbstractBeanConverter<PersistentTask, Task> {

    private DocumentIdentifierConversion identifierConversion;

    /**
     * Creates a new PersistentTaskConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentTaskConverter(ConverterProvider provider) {
        super(provider, PersistentTask.class);
        identifierConversion = new DocumentIdentifierConversion();
    }

    @Override
    public Task convertBean(PersistentTask input) {
        return Task.TaskBuilder.aTask()
                .priority(input.getPriority())
                .workload(input.getWorkload())
                .usecase((UsecaseEntity) identifierConversion.apply(input.getUseCaseId()))
                .build();
    }
}
