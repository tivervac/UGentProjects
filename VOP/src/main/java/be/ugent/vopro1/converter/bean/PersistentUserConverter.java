package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.model.User;

/**
 * Converts {@link PersistentUser} into {@link User}
 */
public class PersistentUserConverter extends AbstractBeanConverter<PersistentUser, User> {

    /**
     * Creates a new PersistentUserConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentUserConverter(ConverterProvider provider) {
        super(provider, PersistentUser.class);
    }

    @Override
    public User convertBean(PersistentUser input) {
        return User.UserBuilder.aUser()
                .email(input.getEmail())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .password(input.getPassword())
                .admin(input.isAdmin())
                .build();
    }
}
