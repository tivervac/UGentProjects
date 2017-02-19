package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.UserEmailConversion;
import be.ugent.vopro1.model.EntityProject;

/**
 * Converts {@link EntityProject} into {@link PersistentProject}
 */
public class ProjectConverter extends AbstractBeanConverter<EntityProject, PersistentProject> {

    private UserEmailConversion emailConversion;

    /**
     * Creates a new ProjectConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public ProjectConverter(ConverterProvider provider) {
        super(provider, EntityProject.class);
        emailConversion = new UserEmailConversion();
    }

    @Override
    public PersistentProject convertBean(EntityProject input) {
        return PersistentProject.PersistentProjectBuilder.aPersistentProject()
                .name(input.getName())
                .leader(emailConversion.apply(input.getLeader().getEmail()).getId())
                .build();
    }
}
