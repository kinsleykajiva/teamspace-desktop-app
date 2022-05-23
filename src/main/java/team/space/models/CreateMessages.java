package team.space.models;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CreateMessages {


    public CreateMessages() {

    }

    public void senderMessage(Message message , VBox MSGS_CONTAINER) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem replyMenu = new MenuItem("Reply");
        MenuItem menuCpoyMessage = new MenuItem("Copy Message");
        MenuItem menuEditMessage = new MenuItem("Edit Message");
        MenuItem menuDeleteMessage = new MenuItem("Delete Message");
        contextMenu.getItems().addAll(replyMenu, menuCpoyMessage, menuEditMessage, menuDeleteMessage);
        //*****************[Center Content]***********************
        Label MSG_LBL = new Label(message.getMessage());
        MSG_LBL.setMinSize(100, Region.USE_PREF_SIZE);
        MSG_LBL.setMaxSize(400, Region.USE_COMPUTED_SIZE);
        MSG_LBL.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LBL.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2) {
                    System.out.println("Double Clicked");
                }
            }
        });
        MSG_LBL.setWrapText(true);
        MSG_LBL.setId("MSG_LBL");


        //*****************[Bottom Bar]***********************
        Label TIME_SENT = new Label(message.getTime());
        TIME_SENT.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT.setId("TIME_SENT");

        ImageView seenImage = new ImageView(new Image(getClass().getResourceAsStream("/images/check3.png")));
        seenImage.setFitHeight(18);
        seenImage.setFitWidth(19);
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
        var xoptions = new ImageView(OPTIONS_IMG);
        xoptions.setFitHeight(18);
        xoptions.setFitWidth(15);




        Button conttextMenue = new Button("", xoptions);
        conttextMenue.setMinSize(0, 0);
        conttextMenue.setMaxSize(10, 30);
        conttextMenue.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        conttextMenue.setTooltip(new Tooltip("Message Options"));
        conttextMenue.setId("conttextMenue");
        conttextMenue.setStyle("-fx-background-color: transparent;");
        conttextMenue.setOnAction(event-> conttextMenue.setContextMenu(contextMenu));
        conttextMenue.setOnMouseClicked(evv-> conttextMenue.setContextMenu(contextMenu));

        HBox MAIN_MSG = new HBox(conttextMenue, MSG_LAYOUT);
        MAIN_MSG.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG.setAlignment(Pos.CENTER_RIGHT);
        MAIN_MSG.setSpacing(5);
        MAIN_MSG.setId("MAIN_MSG");
        MSGS_CONTAINER.getChildren().add(MAIN_MSG);
    }

    public void receiveMessage(Message message , VBox MSGS_CONTAINER) {


        //*****************[Center Content]***********************
        Label MSG_LBL2 = new Label(message.getMessage());
        MSG_LBL2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_PREF_SIZE);
        MSG_LBL2.setMaxSize(270, Region.USE_COMPUTED_SIZE);
        MSG_LBL2.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LBL2.setWrapText(true);
        MSG_LBL2.setId("MSG_LBL2");


        //*****************[Bottom Bar]***********************
        Label TIME_SENT2 = new Label(message.getTime());
        TIME_SENT2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT2.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT2.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        TIME_SENT2.setId("TIME_SENT2");


        BorderPane BOTTOM_BAR_MSG2 = new BorderPane(null, null, null, null, TIME_SENT2);
        BOTTOM_BAR_MSG2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG2.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG2.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        BOTTOM_BAR_MSG2.setId("BOTTOM_BAR_MSG2");


        //*****************[Combining Componentes]***********************
        BorderPane MSG_LAYOUT2 = new BorderPane(null, null, MSG_LBL2, BOTTOM_BAR_MSG2, null);
        MSG_LAYOUT2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT2.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT2.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MSG_LAYOUT2.setId("MSG_LAYOUT2");
        MSG_LAYOUT2.setStyle("-fx-background-color: #4849a1;");

        Image OPTIONS_IMG = new Image(getClass().getResourceAsStream("/images/menu3.png"));

var xoptions = new ImageView(OPTIONS_IMG);
        xoptions.setFitHeight(18);
        xoptions.setFitWidth(15);
        Button OPTIONS_BTN = new Button("", xoptions);
        OPTIONS_BTN.setMinSize(0, 0);
        OPTIONS_BTN.setMaxSize(10, 30);
        OPTIONS_BTN.setStyle("-fx-background-color: transparent;");

        OPTIONS_BTN.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        OPTIONS_BTN.setTooltip(new Tooltip("Message Options"));
        OPTIONS_BTN.setId("OPTIONS_BTN");
       // OPTIONS_BTN.setOnAction(event);

        HBox MAIN_MSG2 = new HBox(MSG_LAYOUT2, OPTIONS_BTN);
        MAIN_MSG2.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG2.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG2.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        MAIN_MSG2.setAlignment(Pos.CENTER_LEFT);
        MAIN_MSG2.setSpacing(5);
        MAIN_MSG2.setId("MAIN_MSG2");

        MSGS_CONTAINER.getChildren().add(MAIN_MSG2);
    }
}
