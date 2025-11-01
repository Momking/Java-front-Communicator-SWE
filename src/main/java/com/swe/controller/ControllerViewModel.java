package com.swe.controller;

import com.swe.controller.Auth.AuthService;
import com.swe.controller.Meeting.MeetingServices;
import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel (VM) for the Controller module.
 * This is a singleton class that holds the application's state and business logic.
 */
public final class ControllerViewModel {

    /** The singleton instance of the ViewModel. */
    private static ControllerViewModel instance;

    /** Handles user registration and authentication. */
    private AuthService authService;
    /** Handles creation and joining of meetings. */
    private final MeetingServices meetingServices;

    /** The networking module implementation (e.g., C# interop). */
    private AbstractController networkController;
    /** The currently logged-in user. */
    private UserProfile currentUser;
    /** The currently active meeting session. */
    private MeetingSession currentMeeting;
    /** The list of participants in the current meeting. */
    private final List<UserProfile> participants;

    /**
     * Private constructor to ensure singleton pattern.
     * Initializes all services and state containers.
     */
    private ControllerViewModel() {
        this.authService = new AuthService();
        this.meetingServices = new MeetingServices();
        this.participants = new ArrayList<>();
        // Set a dummy network controller by default
        this.networkController = new DummyNetworkController();
    }

    /**
     * Gets the single instance of the ControllerViewModel.
     *
     * @return The singleton instance.
     */
    public static ControllerViewModel getInstance() {
        if (instance == null) {
            instance = new ControllerViewModel();
        }
        return instance;
    }

    /**
     * Injects a network controller, typically a mock or dummy for testing.
     *
     * @param controller The network controller to use.
     */
    public void setNetworkController(final AbstractController controller) {
        this.networkController = controller;
    }

    // --- State Accessors ---

    /**
     * Gets the currently logged-in user.
     *
     * @return The current UserProfile, or null if no user is logged in.
     */
    public UserProfile getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Gets the list of participants in the current meeting.
     *
     * @return A list of UserProfiles.
     */
    public List<UserProfile> getParticipants() {
        return this.participants;
    }

    // --- Business Logic "Commands" ---

    /**
     * Registers a new user.
     *
     * @param email       The user's email.
     * @param password    The user's plain-text password.
     * @param displayName The user's display name.
     * @param logoUrl     A URL to the user's logo.
     * @return The new UserProfile if successful, or null if registration fails.
     */
    public UserProfile registerUser(final String email, final String password,
                                    final String displayName, final String logoUrl) {
        final UserProfile user = authService.register(email, password, displayName, logoUrl);
        if (user != null) {
            this.currentUser = user;
        }
        return user;
    }

    /**
     * Creates a new meeting. The current user must be an instructor.
     *
     * @return The new MeetingSession, or null if creation fails.
     */
    public MeetingSession createMeeting() {
        if (this.currentUser == null) {
            System.out.println("Cannot create meeting: No user is logged in.");
            return null; // Must be logged in
        }

        final MeetingSession session = meetingServices.createMeeting(this.currentUser);
        if (session != null) {
            this.currentMeeting = session;
            this.participants.clear();
            this.participants.add(this.currentUser); // Add host as first participant
        } else {
            System.out.println("Failed to create meeting. User might not be an instructor.");
        }
        return session;
    }

    /**
     * Joins an existing meeting.
     *
     * @param meetingId     The ID of the meeting to join.
     * @param serverAddress The address of the main server.
     * @param deviceAddress The address of this user's device.
     * @return True if the join was successful, false otherwise.
     */
    public boolean joinMeeting(final String meetingId, final ClientNode serverAddress,
                               final ClientNode deviceAddress) {
        if (this.currentUser == null) {
            System.out.println("Cannot join meeting: No user is logged in.");
            return false; // Must be logged in
        }

        final boolean joined = meetingServices.joinMeeting(
                this.currentUser,
                meetingId,
                serverAddress,
                deviceAddress,
                this.networkController
        );

        if (joined) {
            // In a real app, we would fetch the participant list here.
            // For now, just add the current user.
            this.participants.add(this.currentUser);
        }
        return joined;
    }

    /**
     * Handles an incoming network message (e.g., from the C# Networking module).
     *
     * @param message The raw message received.
     */
    public void onDataReceived(final String message) {
        // Here you would parse the message (e.g., using Jackson)
        // and update the state.
        System.out.println("VM Received data: " + message);

        // Example: if (message.contains("USER_JOINED")) {
        //   UserProfile p = parseUser(message);
        //   participants.add(p);
        // }
    }

    /**
     * Resets the ViewModel's state.
     * This is intended ONLY for testing to ensure tests are isolated.
     * This method is the fix, as we cannot modify AuthService.
     * It creates a new AuthService, effectively clearing all users.
     */
    public void testOnlyResetState() {
        this.authService = new AuthService();
        this.currentUser = null;
        this.currentMeeting = null;
        this.participants.clear();
        this.networkController = new DummyNetworkController();
    }
}