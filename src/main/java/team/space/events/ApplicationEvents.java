package team.space.events;

import javafx.event.Event;

public interface ApplicationEvents {
    /**
     * Register event handler for event type.
     *
     * @param eventType type
     * @param eventHandler handler
     * @param <T> event
     */
    // <T extends Event> void addEventHandler(EventType<T> eventType,  EventHandler<? super T> eventHandler);
    /**
     * Post (fire) given event. All listening parties will be notified.
     * Events will be handled on the same thread that fired the event,
     * i.e. synchronous.
     *
     * <p>
     *     Note: according to JavaFX doc this must be called on JavaFX Application Thread.
     *     In reality this doesn't seem to be true.
     * </p>
     *
     * @param event the event
     */
    void onFiredEvent(Event event);
}

