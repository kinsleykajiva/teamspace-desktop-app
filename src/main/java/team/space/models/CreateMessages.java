package team.space.models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CreateMessages {
    public void senderMessage(Message message , VBox MSGS_CONTAINER, EventHandler<ActionEvent> event) {
        //*****************[Center Content]***********************
        Label MSG_LBL = new Label(message.getMessage());
        MSG_LBL.setMinSize(100, Region.USE_PREF_SIZE);
        MSG_LBL.setMaxSize(400, Region.USE_COMPUTED_SIZE);
        MSG_LBL.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LBL.setWrapText(true);
        MSG_LBL.setId("MSG_LBL");


        //*****************[Bottom Bar]***********************
        Label TIME_SENT = new Label(message.getTime());
        TIME_SENT.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setId("TIME_SENT");

        ImageView seenImage = new ImageView(new Image(getClass().getResourceAsStream("/images/check3.png")));

        BorderPane BOTTOM_BAR_MSG = new BorderPane(null, null, seenImage, null, TIME_SENT);
        BOTTOM_BAR_MSG.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG.setId("BOTTOM_BAR_MSG");


        //*****************[Combining Componentes]***********************
        BorderPane MSG_LAYOUT = new BorderPane(null, null, MSG_LBL, BOTTOM_BAR_MSG, null);
        MSG_LAYOUT.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT.setId("MSG_LAYOUT");

        Image OPTIONS_IMG = new Image(getClass().getResourceAsStream("/images/menu3.png"));

        Button OPTIONS_BTN = new Button("", new ImageView(OPTIONS_IMG));
        OPTIONS_BTN.setMinSize(0, 0);
        OPTIONS_BTN.setMaxSize(10, 30);
        OPTIONS_BTN.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        OPTIONS_BTN.setTooltip(new Tooltip("Message Options"));
        OPTIONS_BTN.setId("OPTIONS_BTN");
        OPTIONS_BTN.setOnAction(event);

        HBox MAIN_MSG = new HBox(OPTIONS_BTN, MSG_LAYOUT);
        MAIN_MSG.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setAlignment(Pos.CENTER_RIGHT);
        MAIN_MSG.setSpacing(5);
        MAIN_MSG.setId("MAIN_MSG");
        MSGS_CONTAINER.getChildren().add(MAIN_MSG);
    }
}
