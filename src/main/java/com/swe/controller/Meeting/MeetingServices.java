package com.swe.controller.Meeting;

import com.swe.controller.AbstractController;
import com.swe.controller.ClientNode;

import java.util.Optional;

/**
 * Provides high-level logic for creating and joining meetings.
 * This class is used by the ControllerViewModel.
 */
public class MeetingServices {

    /**
     * Default constructor.
     */
    public MeetingServices() {
        // Default constructor
    }

    /**
     * Attempts to create a new meeting.
     * Only users with the "instructor" role can create meetings.
     *
     * @param userParam The user attempting to create the meeting.
     * @return A new MeetingSession if successful, or null otherwise.
     */
    public MeetingSession createMeeting(final UserProfile userParam) {
        if (userParam == null || !"instructor".equals(userParam.getRole())) {
            return null;
        }

        // Call the static create method on MeetingSession
        // which handles the cloud communication.
        // FIXED: This now correctly handles the Optional<MeetingSession>
        Optional<MeetingSession> sessionOptional = MeetingSession.create(userParam);

        // Return the session if present, otherwise null
        return sessionOptional.orElse(null);
    }

    /**
     * Allows a user to join an existing meeting.
     *
     * @param userParam         The user joining.
     * @param meetingIdParam    The ID of the meeting to join.
     * @param clientNode        The server's network address.
     * @param deviceNode        The joining user's network address.
     * @param networkController The network module implementation.
     * @return True if the meeting was found, false otherwise.
     */
    public boolean joinMeeting(
            final UserProfile userParam,
            final String meetingIdParam,
            final ClientNode clientNode,
            final ClientNode deviceNode,
            final AbstractController networkController) {

        // Find the meeting (e.g., by calling the Cloud API)
        final MeetingSession session = MeetingSession.find(meetingIdParam);

        if (session != null) {
            // The meeting exists. Tell the network module to add this user.
            networkController.addUser(deviceNode, clientNode);
            return true;
        }

        // Meeting not found
        return false;
    }
}
