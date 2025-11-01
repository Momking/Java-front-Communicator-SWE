package com.swe.controller;

import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.UserProfile;

/**
 * Main application entry point for testing the ControllerViewModel.
 */
public final class Controller {

    /** The port number for the test device client. */
    private static final int TEST_DEVICE_PORT = 54321;
    /** The port number for the test main server. */
    private static final int TEST_SERVER_PORT = 8080;

    /**
     * Private constructor to prevent instantiation.
     */
    private Controller() {
        // Utility class
    }

    /**
     * Main method to run the simulation.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(final String[] args) {
        System.out.println("--- Controller Application Starting [VM Test] ---");

        // 1. Get the singleton ViewModel instance
        final ControllerViewModel viewModel = ControllerViewModel.getInstance();

        // 2. Create and inject a dummy network module
        final AbstractController dummyNetworkModule = new DummyNetworkController();
        viewModel.setNetworkController(dummyNetworkModule);

        // 3. Define test client/server addresses
        final ClientNode joiningUserAddress = new ClientNode("192.168.1.10", TEST_DEVICE_PORT);
        final ClientNode mainServerAddress = new ClientNode("meet_server_ip", TEST_SERVER_PORT);

        // 4. Simulate user registration
        // FIXED: Call the 4-argument registerUser method
        final UserProfile user = viewModel.registerUser(
                "teacher@iitpkd.ac.in",
                "123456789",
                "Teacher",
                "https://example.com/logo.png"
        );

        if (user != null) {
            System.out.println("User registered successfully: " + user.getUserId());
            // Manually set role to 'instructor' for this test
            user.setRole("instructor");

            // 5. Simulate creating a meeting
            final MeetingSession meeting = viewModel.createMeeting();

            if (meeting != null) {
                System.out.println("Meeting created successfully with ID: " + meeting.getMeetingId());

                // 6. Simulate joining the meeting
                final boolean joined = viewModel.joinMeeting(
                        meeting.getMeetingId(),
                        joiningUserAddress,
                        mainServerAddress
                );

                if (joined) {
                    System.out.println("User joined the meeting successfully.");
                } else {
                    System.out.println("Failed to join the meeting.");
                }
            } else {
                System.out.println("Failed to create meeting. User might not be an instructor.");
            }
        } else {
            System.out.println("User registration failed.");
        }
        System.out.println("--- Controller Application Finished [VM Test] ---");
    }
}

/**
 * Dummy implementation of the AbstractController for testing.
 * This class simulates the behavior of the real network module.
 */
class DummyNetworkController implements AbstractController {
    @Override
    public void addUser(
            final ClientNode deviceAddress,
            final ClientNode mainServerAddress) {
        System.out.println("\n--- DUMMY NETWORK MODULE ---");
        System.out.println("SUCCESS: The addUser method was called correctly.");
        System.out.printf("Received Device Address: %s:%d\n", deviceAddress.hostName(), deviceAddress.port());
        // Wrapped line to fit under 120 chars
        System.out.printf("Received Main Server Address: %s:%d\n",
                mainServerAddress.hostName(), mainServerAddress.port());
        System.out.println("--------------------------\n");
    }
}