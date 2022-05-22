package team.space.controllers;

import javafx.event.Event;
import javafx.stage.Stage;
import team.space.events.ApplicationEvents;

public class SettingsController implements ApplicationEvents {


    private Stage stage;
    private ApplicationEvents applicationEvents;
    public SettingsController(Stage stage, ApplicationEvents applicationEvents) {
        this.stage = stage;
        this.applicationEvents = applicationEvents;
    }




    @Override
    public void onFiredEvent(Event event) {

    }
}
