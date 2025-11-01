package com.swe.controller.Meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

/**
 * Test class for UserProfile.
 * This test is updated to match the 5-argument constructor
 * and methods like getEmail(), getDisplayName(), and toJson().
 */
class UserProfileTest {

    /**
     * A test user object.
     */
    private UserProfile user1;

    /**
     * A second test user object, identical to user1 for equals testing.
     */
    private UserProfile user2;

    /**
     * Jackson mapper for testing JSON serialization.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * A known UUID to make userIds predictable for testing equals().
     */
    private String testUuid;

    /**
     * Sets up common UserProfile objects for testing.
     */
    @BeforeEach
    void setUp() {
        // We use the default constructor and setters for a predictable user
        // to test equals() and hashCode() correctly.
        testUuid = UUID.randomUUID().toString();
        user1 = new UserProfile();
        user1.setUserId(testUuid);
        user1.setEmail("test@example.com");
        user1.setDisplayName("Test User");
        user1.setRole("instructor");
        user1.setLogoUrl("http://example.com/img.png");
        user1.setPasswordHash("hashed_password_123");

        user2 = new UserProfile();
        user2.setUserId(testUuid);
        user2.setEmail("test@example.com");
        user2.setDisplayName("Test User");
        user2.setRole("instructor");
        user2.setLogoUrl("http://example.com/img.png");
        user2.setPasswordHash("hashed_password_123");
    }

    @Test
    @DisplayName("Test 5-Argument Constructor and Getters")
    void testConstructorAndGetters() {
        final UserProfile user = new UserProfile(
                "test@example.com",
                "Test User",
                "hashed_password_123",
                "http://example.com/img.png",
                "instructor"
        );
        assertNotNull(user.getUserId(), "User ID should be generated");
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getDisplayName());
        assertEquals("http://example.com/img.png", user.getLogoUrl());
        assertEquals("instructor", user.getRole());
        assertEquals("hashed_password_123", user.getPasswordHash());
    }

    @Test
    @DisplayName("Test Default Constructor")
    void testDefaultConstructor() {
        final UserProfile user = new UserProfile();
        assertNotNull(user);
    }

    @Test
    @DisplayName("Test Setters")
    void testSetters() {
        final UserProfile user = new UserProfile();
        user.setDisplayName("New Name");
        user.setUserId("user-456");
        user.setEmail("new@example.com");
        user.setLogoUrl("http://new.com/new.jpg");
        user.setRole("student");
        user.setPasswordHash("new_hash");

        assertEquals("New Name", user.getDisplayName());
        assertEquals("user-456", user.getUserId());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("http://new.com/new.jpg", user.getLogoUrl());
        assertEquals("student", user.getRole());
        assertEquals("new_hash", user.getPasswordHash());
    }

    @Test
    @DisplayName("Test Equals and HashCode (based on userId)")
    void testEqualsAndHashCode() {
        // Test for equality (based on the same userId)
        assertEquals(user1, user2, "Users with the same userId should be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal");

        // Test for inequality
        final UserProfile user3 = new UserProfile();
        user3.setUserId(UUID.randomUUID().toString()); // Different userId
        assertNotEquals(user1, user3, "Users with different userIds should not be equal");
        assertNotEquals(user1.hashCode(), user3.hashCode(), "Hash codes should be different");
    }

    @Test
    @DisplayName("Test toString Method Security")
    void testToString() {
        final String userString = user1.toString();
        assertTrue(userString.contains("displayName='Test User'"));
        assertTrue(userString.contains("role='instructor'"));
        // Security Check: Ensure password hash is NOT in the toString output
        assertFalse(userString.contains("hashed_password_123"));
        assertFalse(userString.contains("passwordHash"));
    }

    @Test
    @DisplayName("Test JSON Serialization (Write)")
    void testJsonSerialization() throws Exception {
        final String json = objectMapper.writeValueAsString(user1);

        // Check that keys match the @JsonProperty annotations
        assertTrue(json.contains("\"userId\":\"" + testUuid + "\""));
        assertTrue(json.contains("\"email\":\"test@example.com\""));
        assertTrue(json.contains("\"displayName\":\"Test User\""));
        assertTrue(json.contains("\"logoUrl\":\"http://example.com/img.png\""));

        // This is the check that was failing:
        assertTrue(json.contains("\"role\":\"instructor\""),
                "JSON should contain the 'role' field");

        // Security Check: Ensure password hash is NOT serialized
        assertFalse(json.contains("passwordHash"));
    }

    @Test
    @DisplayName("Test JSON Deserialization (Read)")
    void testJsonDeserialization() throws Exception {
        // JSON string using the keys from your @JsonProperty
        final String json = "{"
                + "\"displayName\":\"JSON User\","
                + "\"userId\":\"user-999\","
                + "\"email\":\"json@example.com\","
                + "\"logoUrl\":\"http://json.com/pic.png\","
                + "\"role\":\"student\","
                + "\"passwordHash\":\"a_hash_that_can_be_read\""
                + "}";

        final UserProfile user = objectMapper.readValue(json, UserProfile.class);

        assertEquals("JSON User", user.getDisplayName());
        assertEquals("user-999", user.getUserId());
        assertEquals("json@example.com", user.getEmail());
        assertEquals("http://json.com/pic.png", user.getLogoUrl());
        assertEquals("student", user.getRole());
        assertEquals("a_hash_that_can_be_read", user.getPasswordHash());
    }

    @Test
    @DisplayName("Test toJson Method")
    void testToJson() {
        final String jsonString = user1.toJson();
        assertTrue(jsonString.contains("\"userId\":\"" + testUuid + "\""));
        assertTrue(jsonString.contains("\"email\":\"test@example.com\""));
        assertTrue(jsonString.contains("\"displayName\":\"Test User\""));
        assertTrue(jsonString.contains("\"logoUrl\":\"http://example.com/img.png\""));
        assertTrue(jsonString.contains("\"role\":\"instructor\""));

        // Security Check: Ensure password hash is NOT in the toJson output
        assertFalse(jsonString.contains("passwordHash"));
    }
}

