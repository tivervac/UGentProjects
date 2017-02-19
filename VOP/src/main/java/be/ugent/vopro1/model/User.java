package be.ugent.vopro1.model;

/**
 * A representation of a User as returned by the database.
 */
public class User implements Cloneable {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Boolean admin;

    /**
     * Default constructor needed for Jackson.
     */
    public User() {
    }

    /**
     * Creates a new User
     *
     * @param email     E-mail of the User
     * @param firstName First name of the User
     * @param lastName  Last name of the User
     * @param password  Hashed password of the User
     * @param admin     Admin status of the User
     * @param analyst   Analyst status of the User
     */
    public User(String email, String firstName, String lastName, String password, boolean admin, boolean analyst) {
        if (email == null || firstName == null || lastName == null || password == null) {
            throw new IllegalArgumentException();
        }

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.admin = admin;
    }

    /**
     * Returns the e-mail of the User
     *
     * @return E-mail of the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the e-mail of this User
     *
     * @param email E-mail to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the first name of the User
     *
     * @return First name of the User
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this User
     *
     * @param firstName First name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the User
     *
     * @return Last name of the User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this User
     *
     * @param lastName Last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the admin status of the User
     *
     * @return Admin status of the User
     */
    public Boolean isAdmin() {
        return admin;
    }

    /**
     * Returns the password of the User
     *
     * @return Password of the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this User
     *
     * @param password Password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the admin status of this User
     *
     * @param admin Admin status to set
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public User clone() {
        return UserBuilder.aUser()
                .email(email)
                .admin(admin)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .build();
    }

    /**
     * Checks if two User's are the same
     *
     * @param o The other User
     * @return If the users are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return !(email != null ? !email.equals(user.email) : user.email != null);

    }

    /**
     * Creates a unique code for this class
     *
     * @return The unique code
     */
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }

    /**
     * Formats all data of this User into a readable format.
     *
     * @return the string representation of this User
     */
    @Override
    public String toString() {
        return "User{"
                + ", email='" + email + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", admin=" + admin
                + '}';
    }

    /**
     * Provides a Builder for {@link User}
     */
    public static class UserBuilder {

        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Boolean admin;

        private UserBuilder() {
        }

        /**
         * Creates a new UserBuilder
         *
         * @return A new UserBuilder
         */
        public static UserBuilder aUser() {
            return new UserBuilder();
        }

        /**
         * Sets the e-mail
         *
         * @param email E-mail to set
         * @return the builder
         */
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the first name
         *
         * @param firstName First name to set
         * @return the builder
         */
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name
         *
         * @param lastName Last name to set
         * @return the builder
         */
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the administrator status
         *
         * @param admin Administrator status to set
         * @return the builder
         */
        public UserBuilder admin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        /**
         * Sets the password
         *
         * @param password Password to set
         * @return the builder
         */
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new UserBuilder with the same values as the current
         * one
         */
        public UserBuilder but() {
            return aUser()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .admin(admin)
                    .password(password);
        }

        /**
         * Creates a User with the current values
         *
         * @return User with the current values
         * @see User
         */
        public User build() {
            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAdmin(admin);
            user.setPassword(password);
            return user;
        }
    }
}
