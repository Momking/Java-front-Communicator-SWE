package com.swe.controller.ux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.swe.controller.Meeting.ParticipantRole;
import com.swe.controller.Meeting.UserProfile;

/**
 * Represents a participant in a meeting session.
 * This wraps UserProfile with additional session-specific information.
 */
public class Participant {
    
    private final UserProfile userProfile;
    private final LocalDateTime joinTime;
    private boolean isMuted;
    private boolean isVideoOn;
    private boolean isHandRaised;
    
    /**
     * Creates a new Participant from a UserProfile.
     *
     * @param profile The user's profile
     */
    public Participant(final UserProfile profile) {
        this.userProfile = profile;
        this.joinTime = LocalDateTime.now();
        this.isMuted = false;
        this.isVideoOn = true;
        this.isHandRaised = false;
    }
    
    /**
     * Creates a new Participant with a specific join time.
     *
     * @param profile The user's profile
     * @param joinTime The time the user joined
     */
    public Participant(final UserProfile profile, final LocalDateTime joinTime) {
        this.userProfile = profile;
        this.joinTime = joinTime;
        this.isMuted = false;
        this.isVideoOn = true;
        this.isHandRaised = false;
    }
    
    // Getters
    
    /**
     * Gets the user profile.
     *
     * @return The user profile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }
    
    /**
     * Gets the user's display name.
     *
     * @return The display name
     */
    public String getDisplayName() {
        return userProfile.getDisplayName();
    }
    
    /**
     * Gets the user's email.
     *
     * @return The email
     */
    public String getEmail() {
        return userProfile.getEmail();
    }
    
    /**
     * Gets the user's role.
     *
     * @return The role
     */
    public ParticipantRole getRole() {
        return userProfile.getRole();
    }
    
    /**
     * Gets the user's logo URL.
     *
     * @return The logo URL
     */
    public String getLogoUrl() {
        return userProfile.getLogoUrl();
    }
    
    /**
     * Gets the join time.
     *
     * @return The join time
     */
    public LocalDateTime getJoinTime() {
        return joinTime;
    }
    
    /**
     * Gets the formatted join time.
     *
     * @return The formatted join time string
     */
    public String getFormattedJoinTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return joinTime.format(formatter);
    }
    
    /**
     * Checks if the participant is muted.
     *
     * @return True if muted
     */
    public boolean isMuted() {
        return isMuted;
    }
    
    /**
     * Checks if the participant's video is on.
     *
     * @return True if video is on
     */
    public boolean isVideoOn() {
        return isVideoOn;
    }
    
    /**
     * Checks if the participant has raised their hand.
     *
     * @return True if hand is raised
     */
    public boolean isHandRaised() {
        return isHandRaised;
    }
    
    // Setters
    
    /**
     * Sets the muted status.
     *
     * @param muted True to mute
     */
    public void setMuted(final boolean muted) {
        this.isMuted = muted;
    }
    
    /**
     * Sets the video status.
     *
     * @param videoOn True to turn video on
     */
    public void setVideoOn(final boolean videoOn) {
        this.isVideoOn = videoOn;
    }
    
    /**
     * Sets the hand raised status.
     *
     * @param handRaised True to raise hand
     */
    public void setHandRaised(final boolean handRaised) {
        this.isHandRaised = handRaised;
    }
    
    @Override
    public String toString() {
        return "Participant{"
                + "displayName='" + getDisplayName() + '\''
                + ", role=" + getRole()
                + ", joinTime=" + getFormattedJoinTime()
                + ", muted=" + isMuted
                + ", videoOn=" + isVideoOn
                + ", handRaised=" + isHandRaised
                + '}';
    }
}

