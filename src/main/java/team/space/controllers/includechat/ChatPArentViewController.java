package team.space.controllers.includechat;

import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import team.space.events.ApplicationEvents;
import team.space.events.MessageEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatPArentViewController implements Initializable, ApplicationEvents {



    private Stage stage;
    private ApplicationEvents applicationEvents;
    public ChatPArentViewController(Stage stage, ApplicationEvents applicationEvents) {
        this.stage = stage;
        this.applicationEvents = applicationEvents;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onFiredEvent(Event event) {

    }


    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {


        System.out.println("Received event: " + event.getEventType());
        // textField.setText(event.message);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
