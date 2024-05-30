package ru.practicum.ewmmain.util;

/**
 * The Marker interface is a marker interface for group annotations used in the validation of incoming data.
 */
public interface Marker {
    /**
     * The OnCreate marker interface is used as a group annotation when creating objects.
     */
    interface OnCreate {
    }

    /**
     * The OnUpdate marker interface is used as a group annotation when updating objects.
     */
    interface OnUpdate {
    }
}
