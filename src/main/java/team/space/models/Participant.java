package team.space.models;



import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//import team.space.beans.StringProperty;

public class Participant {
    private final SimpleStringProperty id;
    private final SimpleStringProperty meetId;
    private final SimpleStringProperty name;

    public Participant(String id, String meetId, String name) {
        this.id = new SimpleStringProperty(id);
        this.meetId = new SimpleStringProperty(meetId);
        this.name = new SimpleStringProperty(name);
    }

    public Participant() {
        this.id = new SimpleStringProperty();
        this.meetId = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public SimpleStringProperty meetIdProperty() {
        return meetId;
    }

    public String getMeetId() {
        return meetId.get();
    }

    public void setMeetId(String meetId) {
        this.meetId.set(meetId);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
