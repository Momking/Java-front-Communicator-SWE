package com.swe.controller;

/**
 * An interface for handling notifications, typically from the
 * networking layer.
 *
 * <p>This is based on the C# INotificationHandler, and allows the
 * networking module to send data (like new messages) to the
 * ViewModel without being tightly coupled.
 */
public interface INotificationHandler {

    /**
     * Called by the networking layer when a new message or
     * data packet is received.
     *
     * @param message The raw data (e.g., JSON string) received.
     */
    void onDataReceived(String message);
}