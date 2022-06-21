package team.space.controllers;

import com.dlsc.formsfx.view.renderer.FormRenderer;
import impl.com.calendarfx.view.AutoScrollPane;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import team.space.events.ApplicationEvents;
import team.space.models.pollsforms.RuntimeFormsModel;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController   implements ApplicationEvents , Initializable  {

    RuntimeFormsModel model;

    @FXML
    public AutoScrollPane bottomSection;
    private Stage stage;
    private ApplicationEvents applicationEvents;
    public HomeController(Stage stage, ApplicationEvents applicationEvents) {
        this.stage = stage;
        this.applicationEvents = applicationEvents;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new RuntimeFormsModel();
      var t=   model.getFormInstance().title("sss");




        bottomSection.setContent(new FormRenderer(t));
    }

    @Override
    public void onFiredEvent(Event event) {

    }
}
