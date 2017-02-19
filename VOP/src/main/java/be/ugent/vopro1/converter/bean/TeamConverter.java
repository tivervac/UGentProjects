package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.conversion.UserEmailConversion;
import be.ugent.vopro1.model.Team;

/**
 * Converts {@link Team} into {@link PersistentTeam}
 */
public class TeamConverter extends AbstractBeanConverter<Team, PersistentTeam> {

    private UserEmailConversion emailConversion;

    /**
     * Creates a new TeamConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public TeamConverter(ConverterProvider provider) {
        super(provider, Team.class);
        emailConversion = new UserEmailConversion();
    }
    
    @Override
    public PersistentTeam convertBean(Team input) {
        return PersistentTeam.PersistentTeamBuilder.aPersistentTeam()
                .name(input.getName())
                .leader(emailConversion.apply(input.getLeader().getEmail()).getId())
                .build();
    }
}
