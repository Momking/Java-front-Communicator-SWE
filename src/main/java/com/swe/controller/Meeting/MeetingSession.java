package com.swe.controller.Meeting;

import com.swe.controller.CloudStorageAdapter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 * Represents an active meeting session.
 * This class holds the runtime state of a meeting,
 * including the host and participants.
 */
public class MeetingSession {

    private final String meetingId;
    private final UserProfile host;
    private final List<UserProfile> participants;
    private final long startTime;

    /**
     * Constructs a new MeetingSession.
     *
     * @param finalHost The UserProfile of the host starting the meeting.
     */
    public MeetingSession(final UserProfile finalHost) {
        this.meetingId = UUID.randomUUID().toString();
        this.host = finalHost;
        this.startTime = System.currentTimeMillis();
        this.participants = new CopyOnWriteArrayList<>();
        this.participants.add(finalHost);
    }

    /**
     * Attempts to create a new meeting session via the cloud adapter.
     *
     * @param finalHostProfile The profile of the host.
     * @return An Optional containing the session, or empty on failure.
     */
    public static Optional<MeetingSession> create(final UserProfile finalHostProfile) {
        final CloudStorageAdapter cloudAdapter = new CloudStorageAdapter("http://localhost:8080");
        try {
            // Call the adapter to create the session in the cloud
            final Optional<MeetingSession> ongoingMeeting = cloudAdapter.createMeetingSession(finalHostProfile);

            if (ongoingMeeting.isPresent()) {
                System.out.println("Cloud session created: " + ongoingMeeting.get().getMeetingId());
                return ongoingMeeting;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to create cloud session: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Finds a meeting session by its ID using the cloud adapter.
     *
     * @param finalMeetingId The ID of the meeting to find.
     * @return The MeetingSession if found, otherwise null.
     */
    public static MeetingSession find(final String finalMeetingId) {
        final CloudStorageAdapter cloudAdapter = new CloudStorageAdapter("http://localhost:8080");
        try {
            // Call the adapter to find the session
            // This is the corrected logic:
            final Optional<MeetingSession> meetingDetails = cloudAdapter.findMeetingSessionById(finalMeetingId);

            if (meetingDetails.isPresent()) {
                return meetingDetails.get();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to find cloud session: " + e.getMessage());
        }
        return null;
    }

    // --- Getters ---

    public String getMeetingId() {
        return meetingId;
    }

    public UserProfile getHost() {
        return host;
    }

    public List<UserProfile> getParticipants() {
        return participants;
    }

    public long getStartTime() {
        return startTime;
    }

    // --- Participant Management ---

    /**
     * Adds a user to the participant list.
     *
     * @param finalUser The user to add.
     */
    public void addUser(final UserProfile finalUser) {
        if (finalUser != null && !participants.contains(finalUser)) {
            participants.add(finalUser);
        }
    }

    /**
     * Removes a user from the participant list.
     *
     * @param finalUser The user to remove.
     */
    public void removeUser(final UserProfile finalUser) {
        if (finalUser != null) {
            participants.remove(finalUser);
        }
    }

    @Override
    public boolean equals(final Object finalO) {
        if (this == finalO) {
            return true;
        }
        if (finalO == null || getClass() != finalO.getClass()) {
            return false;
        }
        final MeetingSession that = (MeetingSession) finalO;
        return Objects.equals(meetingId, that.meetingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId);
    }

    @Override
    public String toString() {
        return "MeetingSession{"
                + "meetingId='" + meetingId + '\''
                + ", host=" + host.getDisplayName()
                + ", participants=" + participants.size()
                + '}';
    }
}

