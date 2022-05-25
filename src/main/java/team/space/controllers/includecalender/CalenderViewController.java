package team.space.controllers.includecalender;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import team.space.beans.DoubleProperty;
import team.space.database.sqlite.DBManager;
import team.space.dto.MeetDto;
import team.space.dto.ParticipantDto;
import team.space.models.Meet;
import team.space.models.Participant;
import team.space.utils.calendar.WeeklyCalendar;


import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static team.space.utils.Shared.LOGGED_USER;

public class CalenderViewController implements Initializable {
    private WeeklyCalendar weeklyCalendar;
    @FXML
    private BorderPane borderPane;
    private Stage stage;
    public CalenderViewController(Stage stage) {

        this.stage = stage;

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            var meetEntries = getMeetEntries();

            weeklyCalendar = new WeeklyCalendar(meetEntries);
            weeklyCalendar.getWeekPage().setPadding(new Insets(10));

            borderPane.setCenter(weeklyCalendar.getWeekPage());
            borderPane.getCenter().setStyle("-fx-border-color: #d9d9d9; -fx-border-width: 1; -fx-border-radius: 10px; -fx-background-radius: 10px");
            borderPane.setPadding(new Insets(0, 50, 50, 50));

            handleCalendarActions();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    private ArrayList<Entry<Meet>> getMeetEntries() throws SQLException {
        return convertMeetsToEntries(getMeets());
    }

    private List<MeetDto> getMeets() throws SQLException {
        return DBManager.getMeetDao().queryForEq("account_id", LOGGED_USER);
    }

    private ArrayList<Entry<Meet>> convertMeetsToEntries(List<MeetDto> meets) {
        var entries = new ArrayList<Entry<Meet>>();

        for (MeetDto meetDto : meets) {
            var startDate = meetDto.getStartDate();
            var endDate = meetDto.getEndDate();

            var entry = new Entry<Meet>(meetDto.getRoom());

            var meet = new Meet(meetDto);

            addMeetListener(meet);

            entry.setUserObject(meet);
            entry.setInterval(startDate.toLocalDateTime(), endDate.toLocalDateTime());

            entries.add(entry);
        }

        return entries;
    }

    private void addMeetListener(Meet meet) {
        meet.getParticipants().addListener((ListChangeListener<? super Participant>) c -> {
            try {
                while (c.next()) {
                    if (c.wasAdded()) {
                        var addedList = c.getAddedSubList();
                        createParticipants(addedList);
                    } else if (c.wasRemoved()) {
                        var removedList = c.getRemoved();
                        removeParticipants(removedList);
                    } else if (c.wasUpdated()) {
                        var participant = meet.getParticipants().get(c.getFrom());
                        updateParticipant(participant);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateParticipant(Participant participant) throws SQLException {
        var updateBuilder = DBManager.getParticipantDao().updateBuilder();

        updateBuilder.where().eq("id", String.valueOf(participant.getId()));
        updateBuilder.updateColumnValue("name", participant.getName());

        updateBuilder.update();
    }

    private void createParticipants(List<? extends Participant> participants) throws SQLException {
        for (Participant participant : participants) {
            var meetDto = DBManager.getMeetDao().queryForId(String.valueOf(participant.getMeetId()));
            var participantDto = new ParticipantDto(participant.getName(), meetDto);

            DBManager.getParticipantDao().create(participantDto);

            participant.setId(participantDto.getId());
        }
    }

    private void removeParticipants(List<? extends Participant> participants) throws SQLException {
        var deleteBuilder = DBManager.getParticipantDao().deleteBuilder();

        for (Participant participant : participants) {
            deleteBuilder.where().eq("id", participant.getId());
        }

        deleteBuilder.delete();
    }

    private Timestamp getEntryStartTimestamp(Entry<Meet> entry) {
        return localDateTimeToTimestamp(entry.getStartAsLocalDateTime());
    }

    private Timestamp getEntryEndTimestamp(Entry<Meet> entry) {
        return localDateTimeToTimestamp(entry.getEndAsLocalDateTime());
    }

    private Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    private void createMeet(Entry<Meet> entry) throws SQLException {
        var startTimestamp = getEntryStartTimestamp(entry);
        var endTimestamp = getEntryEndTimestamp(entry);

        var meetDto = new MeetDto(entry.getTitle(), startTimestamp, endTimestamp, LOGGED_USER);

        DBManager.getMeetDao().create(meetDto);

        var meet = new Meet(meetDto);

        addMeetListener(meet);

        entry.setUserObject(meet);
    }

    private void deleteMeet(Entry<Meet> entry) throws SQLException {
        var deleteBuilder = DBManager.getMeetDao().deleteBuilder();

        var meet = entry.getUserObject();

        deleteBuilder.where().eq("id", meet.getId());

        deleteBuilder.delete();
    }

    private void changeMeetTitle(Entry<Meet> entry) throws SQLException {
        var updateBuilder = DBManager.getMeetDao().updateBuilder();

        var meet = entry.getUserObject();

        updateBuilder.where().eq("id", meet.getId());

        updateBuilder.updateColumnValue("room", entry.getTitle());

        updateBuilder.update();
    }

    private void changeMeetDates(Entry<Meet> entry) throws SQLException {
        var updateBuilder = DBManager.getMeetDao().updateBuilder();

        var meet = entry.getUserObject();

        updateBuilder.where().eq("id", meet.getId());

        updateBuilder.updateColumnValue("startDate", localDateTimeToTimestamp(entry.getStartAsLocalDateTime()));
        updateBuilder.updateColumnValue("endDate", localDateTimeToTimestamp(entry.getEndAsLocalDateTime()));

        updateBuilder.update();
    }

    private void handleCalendarActions() {
        weeklyCalendar.setOnCreateEntryAction(entry -> {
            try {
                createMeet(entry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        weeklyCalendar.setOnUpdateEntryTitleAction(entry -> {
            try {
                changeMeetTitle(entry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        weeklyCalendar.setOnUpdateEntryDatesAction((entry, interval) -> {
            try {
                changeMeetDates(entry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        weeklyCalendar.setOnDeleteEntryAction(entry -> {
            try {
                deleteMeet(entry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
