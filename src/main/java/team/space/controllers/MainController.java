package team.space.controllers;

import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team.space.controllers.includechat.ChatPArentViewController;
import team.space.events.ApplicationEvents;
import team.space.utils.StageManager;

import java.net.URL;
import java.util.ResourceBundle;

import static team.space.utils.ScreenController.loadURL;

public class MainController    implements Initializable, ApplicationEvents {
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public StackPane contentPaneCalling;

    @FXML
    public StackPane contentPane;
    @FXML
    public MFXFontIcon closeIcon;

    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public MFXFontIcon maximiseScreenIcon;



    @FXML
    public VBox mainNav;
    @FXML public ImageView imgHome;
    @FXML public ImageView imgNavChats;
    @FXML public ImageView imgNavCalender;
    @FXML public ImageView imgInBox;
    @FXML public ImageView imgNavSettings;
    @FXML public ImageView imgNavNotifications;
    @FXML public ImageView imgNavLogOut;
 /*   @FXML public ImageView imgInBox;
    @FXML public ImageView imgInBox;*/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClickListener();
        stage = StageManager.getStage();
        for(Node child : mainNav.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }

        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("MESSAGE_CONTROLLER", loadURL("/views/include_chat_parent/incl.chat_parent.fxml")).setControllerFactory(c->new ChatPArentViewController(stage,this)).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("HOME_CONTROLLER", loadURL("/views/homecontroller.fxml")).setControllerFactory(c->new HomeController(stage,this)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SETTINGS_CONTROLLER", loadURL("/views/settingscontroller.fxml")).setControllerFactory(c->new SettingsController(stage,this)).setDefaultRoot(false).get());

        loader.setOnLoadedAction(beans -> beans.forEach(bean -> {

            switch (bean.getViewName()) {
                case "MESSAGE_CONTROLLER" -> imgNavChats.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));
                case "SETTINGS_CONTROLLER" -> imgNavSettings.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));
                case "HOME_CONTROLLER" -> imgHome.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));

            }
            if (bean.isDefaultView()) {
                contentPane.getChildren().setAll(bean.getRoot());
            }
        }));

        loader.start();

    }

    private void initClickListener() {
        maximiseScreenIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (stage.isMaximized()) {
                stage.setMaximized(false);

            } else {
                stage.setMaximized(true);
            }
        });
        closeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.exit());
        minimizeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> ((Stage) rootPane.getScene().getWindow()).setIconified(true));


        contentPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            contentPane.setCursor(Cursor.CLOSED_HAND);
        });
        contentPane.setOnMouseReleased(event -> {
            contentPane.setCursor(Cursor.DEFAULT);
            stage.setOpacity(1.0f);
        });
        contentPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            stage.setOpacity(0.8f);
        });

    }

    @Override
    public void onFiredEvent(Event event) {

    }
}
