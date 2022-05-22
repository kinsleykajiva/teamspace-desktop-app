package team.space.events;


import javafx.event.Event;
import javafx.event.EventType;
import team.space.models.Contact;

public class ChatEvents extends Event {
    public static final EventType<ChatEvents> NEW_CONTACT_EVENT = new EventType<>(Event.ANY, "NEW_CONTACT_EVENT");


    private Contact contact;

    /**
     * for new one contact event
     */
    public ChatEvents(EventType<ChatEvents> eventType, Contact contact) {
        super(eventType);
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
