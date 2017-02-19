package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.UserIdentifierConversion;
import be.ugent.vopro1.model.EntityProject;

/**
 * Converts {@link PersistentProject} into {@link EntityProject}
 */
public class PersistentProjectConverter extends AbstractBeanConverter<PersistentProject, EntityProject> {

    private UserIdentifierConversion identifierConversion;

    /**
     * Creates a new PersistentProjectConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentProjectConverter(ConverterProvider provider) {
        super(provider, PersistentProject.class);
        identifierConversion = new UserIdentifierConversion();
    }
    
    @Override
    public EntityProject convertBean(PersistentProject input) {
        return EntityProject.EntityProjectBuilder.anEntityProject()
                .name(input.getName())
                .leader(identifierConversion.apply(input.getLeaderId()))
                .build();
    }
}
