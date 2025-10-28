package com.swe.controller.Meeting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user's core, permanent profile in the system.
 * This object is the single source of truth for a user's identity.
 * It is created during registration and persists between sessions.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfile {

    /**
     * A unique, non-changing identifier for the user (e.g., a UUID).
     * This is the primary key in the user database.
     */
    @JsonProperty("userId")
    private String userId;

    /**
     * The user's email address, used as their login username.
     */
    @JsonProperty("email")
    private String email;

    /**
     * The user's display name.
     */
    @JsonProperty("displayName")
    private String displayName;

    /**
     * The user's role, either "student" or "instructor".
     */
    @JsonProperty("role")
    private String role;

    /**
     * The URL to the user's profile picture/logo.
     */
    @JsonProperty("logoUrl")
    private String logoUrl;

    /**
     * The securely hashed password.
     * This field is write-only: it can be read from JSON (deserialized)
     * but will NOT be included in JSON output (serialized) for security.
     */
    @JsonProperty(value = "passwordHash", access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    /**
     * Default constructor.
     * Required for frameworks like Jackson for deserialization.
     */
    public UserProfile() {
        // Default constructor
    }

    /**
     * Constructs a new UserProfile.
     * This is the 5-argument constructor expected by AuthService and UserAnalytics.
     *
     * @param finalEmail        The user's unique email address.
     * @param finalDisplayName  The user's display name.
     * @param finalPasswordHash The BCrypt-hashed password.
     * @param finalLogoUrl      The URL for the user's avatar.
     * @param finalUserRole     The user's role (e.g., "student").
     */
    public UserProfile(final String finalEmail,
                       final String finalDisplayName,
                       final String finalPasswordHash,
                       final String finalLogoUrl,
                       final String finalUserRole) {
        this.userId = UUID.randomUUID().toString(); // Generate a unique ID on creation
        this.email = finalEmail;
        this.displayName = finalDisplayName;
        this.role = finalUserRole;
        this.passwordHash = finalPasswordHash;
        this.logoUrl = finalLogoUrl;
    }

    // --- Getters (These match the methods expected by the rest of the code) ---

    /**
     * Gets the user's unique ID.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the user's email address.
     *
     * @return The user email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's display name.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the user's hashed password.
     *
     * @return The hashed password.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Gets the URL of the user's profile picture.
     *
     * @return The profile picture URL.
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * Gets the user's role.
     *
     * @return The user role.
     */
    public String getRole() {
        return role;
    }

    // --- Setters ---

    /**
     * Sets the user's display name.
     *
     * @param finalDisplayName The new display name.
     */
    public void setDisplayName(final String finalDisplayName) {
        this.displayName = finalDisplayName;
    }

    /**
     * Sets the URL of the user's profile picture.
     *
     * @param finalLogoUrl The new profile picture URL.
     */
    public void setLogoUrl(final String finalLogoUrl) {
        this.logoUrl = finalLogoUrl;
    }

    /**
     * Sets the user's unique ID.
     *
     * @param finalUserId The new user ID.
     */
    public void setUserId(final String finalUserId) {
        this.userId = finalUserId;
    }

    /**
     * Sets the user's email address.
     *
     * @param finalEmail The new user email.
     */
    public void setEmail(final String finalEmail) {
        this.email = finalEmail;
    }

    /**
     * Sets the user's role.
     *
     * @param finalRole The new user role.
     */
    public void setRole(final String finalRole) {
        this.role = finalRole;
    }

    /**
     * Sets the user's hashed password.
     *
     * @param finalPasswordHash The new hashed password.
     */
    public void setPasswordHash(final String finalPasswordHash) {
        this.passwordHash = finalPasswordHash;
    }

    // --- Methods expected by DataStore.java ---

    /**
     * Returns a string representation of the UserProfile object for JSON.
     * Note: The passwordHash is intentionally omitted for security.
     *
     * @return a JSON-like string representation
     */
    public String toJson() {
        return "UserProfile{"
                + "\"userId\":\"" + userId + "\","
                + "\"email\":\"" + email + "\","
                + "\"displayName\":\"" + displayName + "\","
                + "\"logoUrl\":\"" + logoUrl + "\","
                + "\"role\":\"" + role + "\""
                + "}";
    }

    // --- Overridden Methods (for Checkstyle and Robustness) ---

    /**
     * Returns a string representation of the UserProfile object for logging.
     * Note: The passwordHash is intentionally omitted for security.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "UserProfile{"
                + "userId='" + userId + '\''
                + ", email='" + email + '\''
                + ", displayName='" + displayName + '\''
                + ", role='" + role + '\''
                + ", logoUrl='" + logoUrl + '\''
                + '}';
    }

    /**
     * Compares this UserProfile to another object for equality.
     *
     * @param obj The object to compare with.
     * @return True if the objects are equal (all fields match), false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserProfile that = (UserProfile) obj;
        return Objects.equals(userId, that.userId)
                && Objects.equals(email, that.email)
                && Objects.equals(displayName, that.displayName)
                && Objects.equals(role, that.role)
                && Objects.equals(logoUrl, that.logoUrl)
                && Objects.equals(passwordHash, that.passwordHash);
    }

    /**
     * Generates a hash code for this UserProfile.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, email, displayName, role, logoUrl, passwordHash);
    }
}