package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.model.User;

/**
 * Converts {@link User} into {@link PersistentUser}
 */
public class UserConverter extends AbstractBeanConverter<User, PersistentUser> {

    /**
     * Creates a new UserConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public UserConverter(ConverterProvider provider) {
        super(provider, User.class);
    }

    @Override
    public PersistentUser convertBean(User input) {
        return PersistentUser.PersistentUserBuilder.aPersistentUser()
                .email(input.getEmail())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .unhashedPassword(input.getPassword())
                .admin(input.isAdmin())
                .build();
    }

}
