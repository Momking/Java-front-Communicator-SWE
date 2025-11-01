package com.swe.controller.Auth;

import com.google.api.client.auth.oauth2.Credential;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import com.swe.controller.Meeting.UserProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for AuthService and GoogleAuthService using real login flow.
 * Email login is automated; Google login opens the browser for user interaction.
 */
public class AuthServicesTest {

    /**
     * Tests email login for a student account.
     * Registers the user and then logs in.
     *
     * @throws Exception if registration or login fails
     */
    @Test
    public void testEmailLoginStudent() throws Exception {
        final DataStore dataStore = new DataStore();
        final AuthService authService = new AuthService();

        authService.register(
                "test_stu_authservice_@smail.iitpkd.ac.in",
                "password123_test_stu_auth_service",
                "Test User Student",
                null
        );

        final UserProfile studentUser = authService.login(
                "test_stu_authservice_@smail.iitpkd.ac.in",
                "password123_test_stu_auth_service"
        );

        assertNotNull(studentUser);
        assertEquals("student", studentUser.getRole().toLowerCase());
        System.out.println("Email Student login: " + studentUser.toJson());
    }

    /**
     * Tests email login for an instructor account.
     * Registers the user and then logs in.
     *
     * @throws Exception if registration or login fails
     */
    @Test
    public void testEmailLoginInstructor() throws Exception {
        final DataStore dataStore = new DataStore();
        final AuthService authService = new AuthService();

        authService.register(
                "test_instructor_authservice_@iitpkd.ac.in",
                "password123_test_inst_auth_service",
                "Test User Instructor",
                null
        );

        final UserProfile instructorUser = authService.login(
                "test_instructor_authservice_@iitpkd.ac.in",
                "password123_test_inst_auth_service"
        );

        assertNotNull(instructorUser);
        assertEquals("instructor", instructorUser.getRole().toLowerCase());
        System.out.println("Email Instructor login: " + instructorUser.toJson());
    }

    /**
     * Tests Google login for a student account.
     * Opens Google login page; user must authenticate manually.
     *
     * @throws Exception if Google login fails
     */
    @Test
    @Disabled("Google OAuth test disabled to prevent browser opening")
    public void testGoogleLoginStudent() throws Exception {
        final DataStore dataStore = new DataStore();
        final GoogleAuthServices googleAuthService = new GoogleAuthServices();
        final Credential credential = googleAuthService.getCredentials();

        final AuthHelper authHelper = new AuthHelper(dataStore);
        final UserProfile googleStudent = authHelper.handleGoogleLogin(credential);

        assertNotNull(googleStudent);
        assertTrue(googleStudent.getEmail().endsWith("@smail.iitpkd.ac.in"));
        assertEquals("student", googleStudent.getRole().toLowerCase());
        System.out.println("Google Student login: " + googleStudent.toJson());
    }
}