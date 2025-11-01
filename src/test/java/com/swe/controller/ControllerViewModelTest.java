package com.swe.controller;

import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

// FIX 1: Replaced star import with explicit imports
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for ControllerViewModel - no servers, no manual intervention.
 */ // FIX 2: Added ending period
class ControllerViewModelTest {

    /** The ViewModel instance under test. */ // FIX 3: Added Javadoc
    private ControllerViewModel viewModel;
    /** A constant port number for test servers. */ // FIX 3: Added Javadoc
    private static final int TEST_SERVER_PORT = 8080;
    /** A constant port number for test devices. */ // FIX 3: Added Javadoc
    private static final int TEST_DEVICE_PORT = 1234;

    @BeforeEach
    void setUp() {
        ControllerViewModel.getInstance().testOnlyResetState();
        viewModel = ControllerViewModel.getInstance();
    }

    @AfterEach
    void tearDown() {
        viewModel.testOnlyResetState();
    }

    @Test
    @DisplayName("Singleton instance should work")
    void testSingletonInstance() {
        final ControllerViewModel instance1 = ControllerViewModel.getInstance();
        final ControllerViewModel instance2 = ControllerViewModel.getInstance();
        
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("Should register instructor with valid email")
    void testRegisterInstructor() {
        final UserProfile result = viewModel.registerUser(
            "professor@iitpkd.ac.in",
            "password123", 
            "Dr. Professor",
            "https://example.com/logo.png"
        );

        assertNotNull(result);
        assertEquals("instructor", result.getRole());
        assertSame(result, viewModel.getCurrentUser());
    }

    @Test
    @DisplayName("Should register student with valid email")
    void testRegisterStudent() {
        final UserProfile result = viewModel.registerUser(
            "student@smail.iitpkd.ac.in",
            "password123",
            "John Student",
            "https://example.com/student.png"
        );

        assertNotNull(result);
        assertEquals("student", result.getRole());
        assertSame(result, viewModel.getCurrentUser());
    }

    @Test
    @DisplayName("Should reject invalid email domain")
    void testRegisterInvalidDomain() {
        final UserProfile result = viewModel.registerUser(
            "user@gmail.com",
            "password123",
            "External User",
            "https://example.com/user.png"
        );

        assertNull(result);
        assertNull(viewModel.getCurrentUser());
    }

    @Test
    @DisplayName("Should reject duplicate email")
    void testRegisterDuplicateEmail() {
        // First registration
        final UserProfile firstUser = viewModel.registerUser(
            "teacher@iitpkd.ac.in",
            "password123",
            "Teacher One",
            "https://example.com/teacher1.png"
        );
        assertNotNull(firstUser);

        // Second registration with same email
        final UserProfile secondUser = viewModel.registerUser(
            "teacher@iitpkd.ac.in", 
            "differentPassword",
            "Teacher Two",
            "https://example.com/teacher2.png"
        );

        assertNull(secondUser);
        assertSame(firstUser, viewModel.getCurrentUser());
    }

    @Test
    @DisplayName("Should fail meeting creation when no user")
    void testCreateMeetingNoUser() {
        final MeetingSession result = viewModel.createMeeting();
        assertNull(result);
    }

    @Test
    @DisplayName("Should fail meeting creation for student")
    void testCreateMeetingStudent() {
        viewModel.registerUser(
            "student@smail.iitpkd.ac.in",
            "password123",
            "Student User",
            "https://example.com/student.png"
        );

        final MeetingSession result = viewModel.createMeeting();
        assertNull(result);
    }

    @Test
    @DisplayName("Should fail join meeting when no user")
    void testJoinMeetingNoUser() {
        final boolean result = viewModel.joinMeeting(
            "test-meeting",
            new ClientNode("server", TEST_SERVER_PORT),
            new ClientNode("device", TEST_DEVICE_PORT)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("Should handle network messages")
    void testOnDataReceived() {
        assertDoesNotThrow(() -> viewModel.onDataReceived("test message"));
    }

    @Test
    @DisplayName("Should return empty participants initially")
    void testGetParticipantsEmpty() {
        final List<UserProfile> participants = viewModel.getParticipants();
        assertNotNull(participants);
        assertTrue(participants.isEmpty());
    }

    @Test
    @DisplayName("Should reset state completely")
    void testResetState() {
        // Setup state
        viewModel.registerUser("teacher@iitpkd.ac.in", "pass", "Teacher", "logo.png");
        viewModel.getParticipants().add(viewModel.getCurrentUser());

        // Verify state exists
        assertNotNull(viewModel.getCurrentUser());
        assertFalse(viewModel.getParticipants().isEmpty());

        // Reset
        viewModel.testOnlyResetState();

        // Verify state cleared
        assertNull(viewModel.getCurrentUser());
        assertTrue(viewModel.getParticipants().isEmpty());
    }

    @Test
    @DisplayName("Should set network controller")
    void testSetNetworkController() {
        final AbstractController controller = new AbstractController() {
            @Override
            public void addUser(final ClientNode deviceNode, final ClientNode clientNode) {
                // Empty implementation for testing
            }
        };

        assertDoesNotThrow(() -> viewModel.setNetworkController(controller));
    }

    @Test
    @DisplayName("Should handle joinMeeting with null parameters")
    void testJoinMeetingNullParams() {
        viewModel.registerUser("teacher@iitpkd.ac.in", "pass", "Teacher", "logo.png");
        
        assertDoesNotThrow(() -> 
            viewModel.joinMeeting(null, null, null)
        );
    }

    @Test
    @DisplayName("Should handle onDataReceived with null")
    void testOnDataReceivedNull() {
        assertDoesNotThrow(() -> viewModel.onDataReceived(null));
    }
}