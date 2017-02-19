package be.ugent.vopro1.bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides a representation of a persistent User.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.UserDAOImpl
 */
public class PersistentUser implements Cloneable {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean admin;
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new PersistentUser and initializes the password encoder
     *
     * @see BCryptPasswordEncoder
     */
    public PersistentUser() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Retrieve the unique identifier of the user
     *
     * @return Identifier of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user. Only use this in DAO classes,
     * nowhere else!
     *
     * @param id New identifier of the user to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the e-mail of the user
     *
     * @return E-mail of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user
     *
     * @param email New email of the user to set
     */
    private void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieve the first name of the user.
     *
     * @return First name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user. Call
     * {@link be.ugent.vopro1.persistence.UserDAO#update} to write this change
     * to the database.
     *
     * @param firstName New first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieve the last name of the user
     *
     * @return Last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user. Call
     * {@link be.ugent.vopro1.persistence.UserDAO#update} to write this change
     * to the database
     *
     * @param lastName New last name of the user to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieve the administrator status of the user
     *
     * @return <code>true</code> if the user is an administrator
     * <code>false</code> otherwise
     */
    public Boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the administrator status of the user. Call
     * {@link be.ugent.vopro1.persistence.UserDAO#update} to write this change
     * to the database
     *
     * @param admin New administrator status to set
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Retrieves the hashed password of the user
     *
     * @return Hashed password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets an already-hashed password for this user
     *
     * @param hashedPassword Hashed password to set
     */
    public void setHashedPassword(String hashedPassword) {
        if (hashedPassword == null) {
            return;
        }

        this.password = hashedPassword;
    }

    /**
     * Sets a unhashed password for this user. It will be hashed first.
     *
     * @param unhashedPassword Unhashed password to hash and set
     */
    public void setUnhashedPassword(String unhashedPassword) {
        if (unhashedPassword == null) {
            return;
        }

        this.password = passwordEncoder.encode(unhashedPassword);
    }

    @Override
    public String toString() {
        return "PersistentUser{"
                + "id=" + id
                + ", email='" + email + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", admin=" + admin
                + '}';
    }

    @Override
    public PersistentUser clone() {
        return PersistentUserBuilder.aPersistentUser()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .admin(admin)
                .hashedPassword(password)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentUser that = (PersistentUser) o;

        if (getId() != that.getId()) {
            return false;
        }
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) {
            return false;
        }
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null) {
            return false;
        }
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null) {
            return false;
        }
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null) {
            return false;
        }
        return !(admin != null ? !admin.equals(that.admin) : that.admin != null);

    }

    @Override
    public int hashCode() {
        final int modifier = 31;
        final int zero = 0;
        int result = getId();
        result = modifier * result + (getEmail() != null ? getEmail().hashCode() : zero);
        result = modifier * result + (getFirstName() != null ? getFirstName().hashCode() : zero);
        result = modifier * result + (getLastName() != null ? getLastName().hashCode() : zero);
        result = modifier * result + (getPassword() != null ? getPassword().hashCode() : zero);
        result = modifier * result + (admin != null ? admin.hashCode() : zero);
        return result;
    }

    /**
     * Provides a Builder for {@link PersistentUser}
     */
    public static class PersistentUserBuilder {

        private int id;
        private String email;
        private String hashedPassword;
        private String unhashedPassword;
        private String firstName;
        private String lastName;
        private Boolean admin;

        private PersistentUserBuilder() {
        }

        /**
         * Creates a new PersistentUserBuilder
         *
         * @return A new PersistentUserBuilder
         */
        public static PersistentUserBuilder aPersistentUser() {
            return new PersistentUserBuilder();
        }

        /**
         * Sets the identifier
         *
         * @param id Identifier to set
         * @return the builder
         */
        public PersistentUserBuilder id(int id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the e-mail
         *
         * @param email E-mail to set
         * @return the builder
         */
        public PersistentUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the first name
         *
         * @param firstName First name to set
         * @return the builder
         */
        public PersistentUserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name
         *
         * @param lastName Last name to set
         * @return the builder
         */
        public PersistentUserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the administrator status
         *
         * @param admin Administrator status to set
         * @return the builder
         */
        public PersistentUserBuilder admin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        /**
         * Sets a hashed password. This will be overridden by unhashed passwords
         * in the final PersistentUser.
         *
         * @param hashedPassword Hashed password to set
         * @return the builder
         */
        public PersistentUserBuilder hashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        /**
         * Sets a unhashed password. This will override any other hashed
         * passwords in the final PersistentUser, as it will be hashed then.
         *
         * @param unhashedPassword Unhashed password to set
         * @return the builder
         */
        public PersistentUserBuilder unhashedPassword(String unhashedPassword) {
            this.unhashedPassword = unhashedPassword;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new PersistentUserBuilder with the same values as the
         * current one
         */
        public PersistentUserBuilder but() {
            return aPersistentUser().id(id)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .admin(admin)
                    .unhashedPassword(unhashedPassword)
                    .hashedPassword(hashedPassword);
        }

        /**
         * Creates a PersistentUser with the current values
         *
         * @return PersistentUser with the current values
         */
        public PersistentUser build() {
            PersistentUser persistentUser = new PersistentUser();
            persistentUser.setId(id);
            persistentUser.setEmail(email);
            persistentUser.setHashedPassword(hashedPassword);
            persistentUser.setUnhashedPassword(unhashedPassword);
            persistentUser.setFirstName(firstName);
            persistentUser.setLastName(lastName);
            persistentUser.setAdmin(admin);
            return persistentUser;
        }
    }
}
