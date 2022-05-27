package team.space.controllers;

import com.jfoenix.controls.JFXSnackbar;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.media.video.VideoDeviceSource;
import dev.onvoid.webrtc.media.video.VideoTrack;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoader;
import io.github.palexdev.materialfx.utils.others.loader.MFXLoaderBean;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import team.space.controllers.includecalender.CalenderViewController;
import team.space.controllers.includechat.ChatPArentViewController;
import team.space.controllers.settings.SettingsViewController;
import team.space.events.ApplicationEvents;
import team.space.events.MessageEvent;
import team.space.models.Contact;
import team.space.utils.MediaUtils;
import team.space.utils.Shared;
import team.space.utils.StageManager;
import team.space.webrtc.janus.Entities.Room;
import team.space.webrtc.janus.Entities.VideoItem;
import team.space.webrtc.janus.utils.JanusClient;
import team.space.webrtc.webrtcutils.CallManager;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static team.space.utils.ScreenController.loadURL;


public class MainController    implements Initializable, ApplicationEvents {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public StackPane  contentPaneCalling1;

    @FXML
    public StackPane contentPane;
    @FXML
    public MFXFontIcon closeIcon;

    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public MFXFontIcon maximiseScreenIcon;

    private MediaUtils  mediaUtils = new MediaUtils();
    @FXML
    public VBox mainNav;
    @FXML public ImageView imgHome ,imgDropCall1 ,imgRingRingUserIcon1 , imgMic1, imgVideConvert1;
    @FXML public ImageView imgNavChats;
    @FXML public ImageView imgNavCalender;
    @FXML public ImageView imgInBox;
    @FXML public ImageView imgNavSettings;
    @FXML public ImageView imgNavNotifications;
    @FXML public ImageView imgNavLogOut;
    @FXML public Label txtCaller1;
    private JFXSnackbar snackbar ;
 /*   @FXML public ImageView imgInBox;
    @FXML public ImageView imgInBox;*/
    private Contact contactInCurrentView;

    public MainController() {
        EventBus.getDefault().register(this);
    }
private CallManager callManager;

    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {


        System.out.println("Received event: " + event.getEventType());

        if( event.getEventType().equals(MessageEvent.MESSAGE_EVENT_MAKE_OUT_GOING_CALL_ALERT) ){
           if( event.getObject() instanceof Contact){
               contentPaneCalling1.setVisible(true);
               // textField.setText(event.message);
               mediaUtils.playIncomingCallAlert();
               txtCaller1.setText( ((Contact) event.getObject()).getFullName() );

               FadeTransition ft = new FadeTransition();
               ft.setDuration(Duration.millis(200));
               ft.setNode(contentPaneCalling1);
               ft.setFromValue(0);
               ft.setToValue(1);
               ft.play();
           }

        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClickListener();
        callManager = new CallManager(contentPaneCalling1);
        contentPaneCalling1.setVisible(false);
        snackbar = new JFXSnackbar(rootPane);
        stage = StageManager.getStage();
        for(Node child : mainNav.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        final int[] loadedCounter = {0};
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("MESSAGE_CONTROLLER", loadURL("/views/include_chat_parent/incl.chat_parent.fxml")).setControllerFactory(c->new ChatPArentViewController(stage,this)).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("CALENDER_CONTROLLER", loadURL("/views/calender/inc.calender.fxml")).setControllerFactory(c->new CalenderViewController(stage)).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("HOME_CONTROLLER", loadURL("/views/homecontroller.fxml")).setControllerFactory(c->new HomeController(stage,this)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SETTINGS_CONTROLLER", loadURL("/views/settings/settings.fxml")).setControllerFactory(c->new SettingsViewController()).setDefaultRoot(false).get());

        loader.setOnLoadedAction(beans -> beans.forEach(bean -> {

            switch (bean.getViewName()) {
                case "MESSAGE_CONTROLLER" -> imgNavChats.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));
                case "SETTINGS_CONTROLLER" -> imgNavSettings.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));
                case "HOME_CONTROLLER" -> imgHome.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));
                case "CALENDER_CONTROLLER" -> imgNavCalender.setOnMouseClicked(event -> contentPane.getChildren().setAll(bean.getRoot()));

            }
            if (bean.isDefaultView()) {
                contentPane.getChildren().setAll(bean.getRoot());
            }
            loadedCounter[0]++;
            if(loadedCounter[0] == beans.size()) {
                System.out.println("Views are  ready " );
                mediaUtils.playWelcomeAlert();
                JFXSnackbar.SnackbarEvent snackbarEvent = new JFXSnackbar.SnackbarEvent(new Label("Views are ready"), Duration.seconds(13.33), null);
                snackbar.enqueue(snackbarEvent);
            }
           // System.out.println("Loaded view: Done - " +loadedCounter[0] );

        }));

        loader.start();
// mediaUtils


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

        imgDropCall1.setOnMouseClicked(event -> {
            System.out.println("dragged");
            mediaUtils.stopIncomingCallAlert();

            contentPaneCalling1.setVisible(false);
        });

        imgMic1.setOnMouseClicked(event -> {
            System.out.println("dragged");
            mediaUtils.stopIncomingCallAlert();

            contentPaneCalling1.setVisible(false);
        });
        imgVideConvert1.setOnMouseClicked(event -> {
            System.out.println("dragged");
            mediaUtils.stopIncomingCallAlert();

            contentPaneCalling1.setVisible(false);



            FadeTransition ft = new FadeTransition();
            ft.setDuration(Duration.millis(200));
            ft.setNode(contentPaneCalling1);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
        });

    }

    @Override
    public void onFiredEvent(Event event) {

    }
}
