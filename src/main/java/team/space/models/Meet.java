package team.space.models;

/*import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;*/

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import team.space.dto.MeetDto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Meet {
    private final SimpleStringProperty id;
    private final SimpleStringProperty accountId;
    private final StringProperty room;
    private final ObjectProperty<Timestamp> startDate;
    private final ObjectProperty<Timestamp> endDate;
    private ObservableList<Participant> participants;

    public Meet(MeetDto meetDto) {
        this(
                meetDto.getId_(),
                meetDto.getAccount().getUserId(),
                meetDto.getRoom(),
                meetDto.getStartDate(),
                meetDto.getEndDate(),
                meetDto.getParticipants() != null
                        ? (new ArrayList<>(meetDto.getParticipants()))
                        .stream()
                        .map(participantDto -> new Participant(
                                participantDto.getId(),
                                participantDto.getMeet().getId_(),
                                participantDto.getName())
                        )
                        .collect(Collectors.toList())
                        : null
        );

    }

    private Meet(String id, String accountId, String room, Timestamp startDate, Timestamp endDate, List<Participant> participants) {
        Callback<Participant, Observable[]> extractor = participant -> new Observable[]{participant.nameProperty()};

        this.id = new SimpleStringProperty(id);
        this.accountId = new SimpleStringProperty(accountId);
        this.room = new SimpleStringProperty(room);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);

        this.participants = FXCollections.observableArrayList(extractor);

        if (participants != null) {
            this.participants.addAll(participants);
        }
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public Timestamp getStartDate() {
        return startDate.get();
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate.set(startDate);
    }

    public Timestamp getEndDate() {
        return endDate.get();
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate.set(endDate);
    }

    public ObservableList<Participant> getParticipants() {
        return participants;
    }
}
