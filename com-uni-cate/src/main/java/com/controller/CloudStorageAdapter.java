package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Implements communication with the remote Cloud Storage module's API.
 */
public class CloudStorageAdapter {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String cloudApiBaseUrl;

    public CloudStorageAdapter(String cloudApiBaseUrl) {
        this.cloudApiBaseUrl = cloudApiBaseUrl;
    }

    /**
     * Asks the Cloud module to create a new meeting session.
     * Corresponds to: POST /api/sessions
     *
     * @param hostProfile The profile of the user starting the meeting.
     * @return An Optional containing the created MeetingSession, or empty on failure.
     */
    public Optional<MeetingSession> createMeetingSession(UserProfile hostProfile) throws IOException, InterruptedException {
        // The Cloud API likely expects the host's info in the request body.
        String requestBody = objectMapper.writeValueAsString(hostProfile);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cloudApiBaseUrl + "/api/sessions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getCloudServiceAuthToken())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) { // 201 Created
            MeetingSession session = objectMapper.readValue(response.body(), MeetingSession.class);
            return Optional.of(session);
        }
        return Optional.empty();
    }

    /**
     * Asks the Cloud module if a meeting session exists.
     * Corresponds to: GET /api/sessions/{sessionId}
     *
     * @param sessionId The ID of the meeting to check.
     * @return An Optional containing the MeetingSession if found, otherwise empty.
     */
    public Optional<MeetingSession> findMeetingSessionById(String sessionId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cloudApiBaseUrl + "/api/sessions/" + sessionId))
                .header("Authorization", "Bearer " + getCloudServiceAuthToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) { // 200 OK
            MeetingSession session = objectMapper.readValue(response.body(), MeetingSession.class);
            return Optional.of(session);
        }
        return Optional.empty();
    }

    private String getCloudServiceAuthToken() {
        return "super-secret-service-to-service-token";
    }
}

