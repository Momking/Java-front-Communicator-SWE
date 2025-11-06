package com.swe.controller.ux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.ParticipantRole;
import com.swe.controller.Meeting.UserProfile;

/**
 * ViewModel for the participants list.
 * Manages the data and business logic for displaying meeting participants.
 */
public class ParticipantsListViewModel {
    
    private final List<Participant> participants;
    private final List<ParticipantListListener> listeners;
    
    /**
     * Creates a new ParticipantsListViewModel.
     */
    public ParticipantsListViewModel() {
        this.participants = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }
    
    /**
     * Adds a participant to the list.
     *
     * @param participant The participant to add
     */
    public void addParticipant(final Participant participant) {
        if (participant != null && !participants.contains(participant)) {
            participants.add(participant);
            notifyParticipantsChanged();
        }
    }
    
    /**
     * Adds a participant from a UserProfile.
     *
     * @param userProfile The user profile
     */
    public void addParticipant(final UserProfile userProfile) {
        if (userProfile != null) {
            addParticipant(new Participant(userProfile));
        }
    }
    
    /**
     * Removes a participant from the list.
     *
     * @param emailId The ID of the participant to remove
     * @return True if removed successfully
     */
    public boolean removeParticipant(final String emailId) {
        boolean removed = participants.removeIf(p -> 
            p.getEmail().equals(emailId)
        );
        if (removed) {
            notifyParticipantsChanged();
        }
        return removed;
    }
    
    /**
     * Gets all participants.
     *
     * @return Unmodifiable list of participants
     */
    public List<Participant> getParticipants() {
        return Collections.unmodifiableList(participants);
    }
    
    /**
     * Gets the count of participants.
     *
     * @return The number of participants
     */
    public int getParticipantCount() {
        return participants.size();
    }
    
    /**
     * Gets participants filtered by role.
     *
     * @param role The role to filter by
     * @return List of participants with the specified role
     */
    public List<Participant> getParticipantsByRole(final ParticipantRole role) {
        return participants.stream()
                .filter(p -> p.getRole() == role)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the count of instructors.
     *
     * @return The number of instructors
     */
    public int getInstructorCount() {
        return (int) participants.stream()
                .filter(p -> p.getRole() == ParticipantRole.INSTRUCTOR)
                .count();
    }
    
    /**
     * Gets the count of students.
     *
     * @return The number of students
     */
    public int getStudentCount() {
        return (int) participants.stream()
                .filter(p -> p.getRole() == ParticipantRole.STUDENT)
                .count();
    }
    
    /**
     * Finds a participant by user ID.
     *
     * @param emailId The user ID to search for
     * @return The participant, or null if not found
     */
    public Participant findParticipantById(final String emailId) {
        return participants.stream()
                .filter(p -> p.getEmail().equals(emailId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Clears all participants.
     */
    public void clearParticipants() {
        participants.clear();
        notifyParticipantsChanged();
    }
    
    /**
     * Loads mock participants for testing.
     */
    public void loadParticipants(MeetingSession meetingSession) {
        Map<String, UserProfile>  meetUsers = meetingSession.getParticipants();
        for (Map.Entry<String, UserProfile> entry : meetUsers.entrySet()) {
            UserProfile user = entry.getValue();
            addParticipant(user);
        }
    }
    
    /**
     * Adds a listener for participant list changes.
     *
     * @param listener The listener to add
     */
    public void addListener(final ParticipantListListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Removes a listener.
     *
     * @param listener The listener to remove
     */
    public void removeListener(final ParticipantListListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notifies all listeners that the participant list has changed.
     */
    private void notifyParticipantsChanged() {
        for (ParticipantListListener listener : listeners) {
            listener.onParticipantsChanged(getParticipants());
        }
    }
    
    /**
     * Listener interface for participant list changes.
     */
    public interface ParticipantListListener {
        /**
         * Called when the participant list changes.
         *
         * @param participants The updated list of participants
         */
        void onParticipantsChanged(List<Participant> participants);
    }
}

