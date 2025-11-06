package com.swe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swe.controller.Meeting.MeetingSession;
import com.swe.controller.Meeting.ParticipantRole;
import com.swe.controller.Meeting.SessionMode;
import com.swe.controller.Meeting.UserProfile;
import com.swe.controller.RPCinterface.AbstractRPC;
import com.swe.controller.serialize.DataSerializer;

public class controllerServices {

    AbstractRPC rpc;

    UserProfile profile;

    MeetingSession meetingSession;

    controllerServices(AbstractRPC rpc) {
        this.rpc = rpc;
    }

    private void register() throws JsonProcessingException {
        rpc.call(rpcUtils.REGISTER, DataSerializer.serializeParticipantsList(ParticipantRole.GUEST)).thenAccept(response -> {
            try {
                profile = DataSerializer.deserializeParticipantsList(response, UserProfile.class);
                System.out.println("Registered user with email: " + profile.getEmail());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createMeeting(SessionMode MeetingMode) throws JsonProcessingException {
        if (meetingSession != null){
            System.out.println("You are already inside a meet");
            return;
        }
        if(profile == null){
            System.out.println("You are not registered!!!");
            return;
        }

        rpc.call(rpcUtils.CREATE_MEETING, DataSerializer.serializeParticipantsList(MeetingMode)).thenAccept(response -> {
            try {
                meetingSession = DataSerializer.deserializeParticipantsList(response, MeetingSession.class);
                System.out.println("Created meeting at: " + meetingSession.getCreatedAt());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void joinMeeting(String meetId) throws JsonProcessingException {
        if (meetingSession != null){
            System.out.println("You are already inside a meet");
            return;
        }
        if(profile == null){
            System.out.println("You are not registered!!!");
            return;
        }

        rpc.call(rpcUtils.JOIN_MEETING, DataSerializer.serializeParticipantsList(meetId)).thenAccept(response -> {
            try {
                meetingSession = DataSerializer.deserializeParticipantsList(response, MeetingSession.class);
                System.out.println("Joined a meet created by: " + meetingSession.getCreatedBy());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void runController() {
        System.out.println("running controller");

        profile = new UserProfile("student1@example.com",
                "Hij Klm",
                "https://i.pravatar.cc/150?img=5",
                ParticipantRole.STUDENT);

        while (true) {}
    }
}

