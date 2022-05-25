package team.space.controllers.includecalender;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import team.space.beans.DoubleProperty;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CalenderViewController implements Initializable {
    private CalendarView calendarView;
    private @FXML AnchorPane anchorPaneRoot;
    Stage stage;
    public CalenderViewController(Stage stage) {

        this.stage = stage;

    }

    protected DateControl createControl() {
        calendarView = new CalendarView();
        CalendarSource calendarSource = new CalendarSource("My Calendars");
        // calendarSource.getCalendars().add(new HelloCalendar());


        calendarView.getCalendarSources().add(calendarSource);
        calendarView.setTransitionsEnabled(true);
        calendarView.setRequestedTime(LocalTime.now());

        return calendarView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


      /*  var oldW =  anchorPaneRoot.getPrefWidth() ;
        var oldH =  anchorPaneRoot.getPrefHeight() ;*/
        var oldX =  anchorPaneRoot.getLayoutX() ;
        var oldY =  anchorPaneRoot.getLayoutY() ;

      //  calendarView.getLayoutBounds().
        anchorPaneRoot.getChildren().add(createControl());



        stage.heightProperty().addListener(e ->{
           System.out.println("height");
            var oldW =  anchorPaneRoot.getPrefWidth() ;
            var oldH =  anchorPaneRoot.getPrefHeight() ;
            calendarView.setPrefWidth(oldW );
            calendarView.setPrefHeight(oldH );
        });

        stage.widthProperty().addListener(e ->{
            System.out.println("width");
            var oldW =  anchorPaneRoot.getPrefWidth() ;
            var oldH =  anchorPaneRoot.getPrefHeight() ;

            calendarView.setPrefWidth(oldW );
            calendarView.setPrefHeight(oldH );
        });
        var oldW =  anchorPaneRoot.getPrefWidth() ;
        var oldH =  anchorPaneRoot.getPrefHeight() ;
        calendarView.setPrefWidth(oldW -80);
        calendarView.setPrefHeight(oldH -80);
    }
}
