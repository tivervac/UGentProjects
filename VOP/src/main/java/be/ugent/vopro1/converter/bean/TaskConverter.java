package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.DocumentNameConversion;
import be.ugent.vopro1.model.Task;

/**
 * Converts {@link Task} into {@link PersistentTask}
 */
public class TaskConverter extends AbstractBeanConverter<Task, PersistentTask> {

    private DocumentNameConversion nameConversion;

    /**
     * Creates a new TaskConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public TaskConverter(ConverterProvider provider) {
        super(provider, Task.class);
        this.nameConversion = new DocumentNameConversion();
    }

    @Override
    public PersistentTask convertBean(Task input) {
        return new PersistentTask(nameConversion.apply(input.getUsecase()), input.getWorkload(), input.getPriority());
    }
}
