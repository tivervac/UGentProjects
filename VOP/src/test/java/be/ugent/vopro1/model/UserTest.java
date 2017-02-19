package be.ugent.vopro1.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user;

    @Before
    public void initialize() {
        user = User.UserBuilder.aUser()
                .admin(true)
                .email("lol@code.com")
                .firstName("first")
                .lastName("last")
                .password("you-shall-not-pass")
                .build();
    }

    @Test
    public void testProperties() {
        assertEquals(true, user.isAdmin());
        user.setAdmin(false);
        assertEquals(false, user.isAdmin());

        assertEquals("lol@code.com", user.getEmail());
        user.setEmail("me@example.com");
        assertEquals("me@example.com", user.getEmail());

        assertEquals("first", user.getFirstName());
        user.setFirstName("firstName");
        assertEquals("firstName", user.getFirstName());

        assertEquals("last", user.getLastName());
        user.setLastName("lastName");
        assertEquals("lastName", user.getLastName());

        assertEquals("you-shall-not-pass", user.getPassword());
        user.setPassword("ok-i-wont");
        assertEquals("ok-i-wont", user.getPassword());
    }

}
