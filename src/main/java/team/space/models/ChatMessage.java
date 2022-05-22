package team.space.models;


import java.time.LocalTime;

public abstract class ChatMessage {

    public enum Origin {

        LOCAL,

        REMOTE;

    }

    private transient Origin origin;

    private transient LocalTime time;


    public ChatMessage() {
        this(Origin.LOCAL, LocalTime.now());
    }

    public ChatMessage(Origin origin, LocalTime time) {
        this.origin = origin;
        this.time = time;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}

