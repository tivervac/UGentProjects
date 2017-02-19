package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.UserIdentifierConversion;
import be.ugent.vopro1.model.Team;

/**
 * Converts {@link PersistentTeam} into {@link Team}
 */
public class PersistentTeamConverter extends AbstractBeanConverter<PersistentTeam, Team> {

    private UserIdentifierConversion identifierConversion;

    /**
     * Creates a new PersistentTeamConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentTeamConverter(ConverterProvider provider) {
        super(provider, PersistentTeam.class);
        identifierConversion = new UserIdentifierConversion();
    }

    @Override
    public Team convertBean(PersistentTeam input) {
        return Team.TeamBuilder.aTeam()
                .name(input.getName())
                .leader(identifierConversion.apply(input.getLeaderId()))
                .build();
    }
}
