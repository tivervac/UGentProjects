package be.ugent.vopro1.interactor;

import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandler;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandlerImpl;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.UserDAOFactory;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOAuthMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderAuthenticationHandlerTests {

    HeaderAuthenticationHandler handler;
    private static final String ADMIN_HEADER = "Basic YWRtaW5AZXhhbXBsZS5jb206bG9sY29kZQ==";
    private static final String USER_HEADER = "Basic dXNlckBleGFtcGxlLmNvbTpsb2xjb2Rl";
    private static final String OTHER_HEADER = "Basic b3RoZXJAZXhhbXBsZS5jb206bG9sY29kZQ";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String USER_EMAIL = "user@example.com";
    private static final String OTHER_EMAIL = "other@example.com";
    private static final String PASSWORD = "lolcode";

    @Before
    public void setUp() throws Exception {
        UserDAO daoMock = new UserDAOAuthMock();
        UserDAOFactory userDAOFactory = new UserDAOFactory();
        userDAOFactory.setInstance(daoMock);
        DAOProvider.setInstance("user", userDAOFactory);
        handler = new HeaderAuthenticationHandlerImpl();
    }

    @After
    public void tearDown() throws Exception {
        DAOProvider.setDefault();
    }

    @Test
    public void testHasPermission() throws Exception {
        // EVERYTHING OK
        assertTrue(handler.hasPermission(ADMIN_HEADER));
        assertTrue(handler.hasPermission(USER_HEADER));
        assertTrue(handler.hasPermission(OTHER_HEADER));
    }

    @Test
    public void testHasPermission1() throws Exception {
        // VALID USER REQUIRED
        assertTrue(handler.hasPermission(ADMIN_HEADER, true));
        assertTrue(handler.hasPermission(USER_HEADER, true));
        assertFalse(handler.hasPermission(OTHER_HEADER, true));

        assertTrue(handler.hasPermission(OTHER_HEADER, false));
    }

    @Test
    public void testHasPermission2() throws Exception {
        // ADMIN REQUIRED
        assertTrue(handler.hasPermission(ADMIN_HEADER, true, true));
        assertFalse(handler.hasPermission(USER_HEADER, true, true));
        assertFalse(handler.hasPermission(OTHER_HEADER, true, true));

        assertTrue(handler.hasPermission(USER_HEADER, true, false));
        assertFalse(handler.hasPermission(OTHER_HEADER, true, false));

    }

    @Test
    public void testHasPermission4() throws Exception {
        // EVERYTHING OK
        assertTrue(handler.hasPermission(ADMIN_EMAIL, PASSWORD));
        assertTrue(handler.hasPermission(USER_EMAIL, PASSWORD));
        assertTrue(handler.hasPermission(OTHER_EMAIL, PASSWORD));
    }

    @Test
    public void testHasPermission5() throws Exception {
        // VALID USER REQUIRED
        assertTrue(handler.hasPermission(ADMIN_EMAIL, PASSWORD, true));
        assertTrue(handler.hasPermission(USER_EMAIL, PASSWORD, true));
        assertFalse(handler.hasPermission(OTHER_EMAIL, PASSWORD, true));

        assertTrue(handler.hasPermission(OTHER_EMAIL, PASSWORD, false));
    }

    @Test
    public void testHasPermission6() throws Exception {
        // ADMIN REQUIRED
        assertTrue(handler.hasPermission(ADMIN_EMAIL, PASSWORD, true, true));
        assertFalse(handler.hasPermission(USER_EMAIL, PASSWORD, true, true));
        assertFalse(handler.hasPermission(OTHER_EMAIL, PASSWORD, true, true));

        assertTrue(handler.hasPermission(USER_EMAIL, PASSWORD, true, false));
        assertFalse(handler.hasPermission(OTHER_EMAIL, PASSWORD, true, false));
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals(ADMIN_EMAIL, handler.getEmail(ADMIN_HEADER));
        assertEquals(USER_EMAIL, handler.getEmail(USER_HEADER));
        assertEquals(OTHER_EMAIL, handler.getEmail(OTHER_HEADER));
    }
}