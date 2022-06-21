package team.space.controllers;

import com.jfoenix.controls.JFXSnackbar;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCIceServer;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.media.MediaDevices;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.video.VideoDevice;
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
import org.json.JSONArray;
import org.json.JSONObject;
import team.space.controllers.filesmanager.FileManagerViewController;
import team.space.controllers.includecalender.CalenderViewController;
import team.space.controllers.includechat.ChatPArentViewController;
import team.space.controllers.settings.SettingsViewController;
import team.space.events.ApplicationEvents;
import team.space.events.MessageEvent;
import team.space.models.Contact;
import team.space.requests.getallusers.User;
import team.space.utils.MediaUtils;
import team.space.utils.Shared;
import team.space.utils.StageManager;
import team.space.webrtc.janus.Entities.Room;
import team.space.webrtc.janus.Entities.VideoItem;
import team.space.webrtc.janus.clientapi.*;
import team.space.webrtc.janus.utils.JanusClient;
import team.space.webrtc.janus.utils.VideoView;
import team.space.webrtc.webrtcutils.CallManager;
import team.space.webrtc.webrtcutils.GroupVideoCall;
import team.space.webrtc.webrtcutils.WebRTCUtils;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static team.space.utils.ScreenController.loadURL;


public class MainController implements Initializable, ApplicationEvents {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public StackPane contentPaneCalling1;

    @FXML
    public StackPane contentPane;
    @FXML
    public MFXFontIcon closeIcon;

    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public MFXFontIcon maximiseScreenIcon;

    private MediaUtils mediaUtils = new MediaUtils();
    @FXML
    public VBox mainNav, sideProfileVBox ,imgNavSettingsVBox ,imgNavFilesVBox ,imgNavNotificationsVBox,   imgNavChatsVBox, imgNavCalenderVBox, imgHomeVBox;
    @FXML
    public ImageView imgHome, imgDropCall1, imgRingRingUserIcon1, imgMic1, imgVideConvert1;
    @FXML
    public ImageView imgNavChats;
    @FXML
    public ImageView imgNavCalender ,imgNavFiles;
    @FXML
    public ImageView imgInBox;
    @FXML
    public ImageView imgNavSettings;
    @FXML
    public ImageView imgNavNotifications;
    @FXML
    public ImageView imgNavLogOut;
    @FXML
    public Label txtCaller1;
    private JFXSnackbar snackbar;
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

        if (event.getEventType().equals(MessageEvent.MESSAGE_EVENT_MAKE_OUT_GOING_CALL_ALERT)) {
            if (event.getObject() instanceof Contact) {
                contentPaneCalling1.setVisible(true);
                // textField.setText(event.message);
                //   mediaUtils.playIncomingCallAlert();
                txtCaller1.setText(((Contact) event.getObject()).getFullName());

                FadeTransition ft = new FadeTransition();
                ft.setDuration(Duration.millis(200));
                ft.setNode(contentPaneCalling1);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }

        }

    }
    List<VBox> vBoxes ; GroupVideoCall groupVideoCall;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClickListener();
        callManager = new CallManager(sideProfileVBox);
         contentPaneCalling1.setVisible(false);
        snackbar = new JFXSnackbar(rootPane);
        stage = StageManager.getStage();
        for (Node child : mainNav.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        final int[] loadedCounter = {0};
        MFXLoader loader = new MFXLoader();
        loader.addView(MFXLoaderBean.of("MESSAGE_CONTROLLER", loadURL("/views/include_chat_parent/incl.chat_parent.fxml")).setControllerFactory(c -> new ChatPArentViewController(stage, this)).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("CALENDER_CONTROLLER", loadURL("/views/calender/inc.calender.fxml")).setControllerFactory(c -> new CalenderViewController(stage)).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("HOME_CONTROLLER", loadURL("/views/homecontroller.fxml")).setControllerFactory(c -> new HomeController(stage, this)).setDefaultRoot(true).get());
        loader.addView(MFXLoaderBean.of("SETTINGS_CONTROLLER", loadURL("/views/settings/settings.fxml")).setControllerFactory(c -> new SettingsViewController()).setDefaultRoot(false).get());
        loader.addView(MFXLoaderBean.of("FILES_CONTROLLER", loadURL("/views/include_files_manager/include.files_parent.fxml")).setControllerFactory(c -> new FileManagerViewController()).setDefaultRoot(false).get());

        loader.setOnLoadedAction(beans -> beans.forEach(bean -> {

            switch (bean.getViewName()) {
                case "MESSAGE_CONTROLLER" -> imgNavChats.setOnMouseClicked(event -> {
                    contentPane.getChildren().setAll(bean.getRoot());
                    setSelcted(imgNavChatsVBox);
                });
                case "SETTINGS_CONTROLLER" -> imgNavSettings.setOnMouseClicked(event -> {
                    contentPane.getChildren().setAll(bean.getRoot());
                    setSelcted(imgNavSettingsVBox);
                });
                case "HOME_CONTROLLER" -> imgHome.setOnMouseClicked(event -> {
                    contentPane.getChildren().setAll(bean.getRoot());
                    setSelcted(imgHomeVBox);
                });
                case "CALENDER_CONTROLLER" -> imgNavCalender.setOnMouseClicked(event ->  {contentPane.getChildren().setAll(bean.getRoot()); setSelcted(imgNavCalenderVBox);    });
                case "FILES_CONTROLLER" -> imgNavFiles.setOnMouseClicked(event -> {
                    contentPane.getChildren().setAll(bean.getRoot());
                    setSelcted(imgNavFilesVBox);
                });

            }
            if (bean.isDefaultView()) {
                contentPane.getChildren().setAll(bean.getRoot());
            }
            loadedCounter[0]++;
            if (loadedCounter[0] == beans.size()) {
                System.out.println("Views are  ready ");
                mediaUtils.playWelcomeAlert();
                JFXSnackbar.SnackbarEvent snackbarEvent = new JFXSnackbar.SnackbarEvent(new Label("Views are ready"), Duration.seconds(13.33), null);
                snackbar.enqueue(snackbarEvent);
            }
            // System.out.println("Loaded view: Done - " +loadedCounter[0] );

        }));

        loader.start();
        VideoView videoView = new VideoView();
        videoView.resize(640, 480);
// mediaUtils
        callManager.initJanusWebrtcSession();
        HashMap<Integer, User> groupVideoCallUsers = new HashMap<>();
        User sender = new User();
        sender.setId_("TTTTTT");
        createRenderer();
        vBoxes = List.of( imgNavCalenderVBox , imgNavChatsVBox, imgHomeVBox ,  imgNavNotificationsVBox ,imgNavSettingsVBox ,  imgNavFilesVBox);
       // groupVideoCall = new GroupVideoCall( localRender, remoteRenders,sender, groupVideoCallUsers , 1234);
        // janusServer = new JanusServer(new JanusGlobalCallbacks());
      //  janusServer.Connect();
        System.out.println("MainController initialized");
       // groupVideoCall.Start();

    }
    HashMap<Integer, User> receiversHashMap; //  영상


    private VideoView localRender =new VideoView();
    ArrayList<VideoView> remoteRenders = new ArrayList<>();
    //private VideoRenderer.Callbacks localRender;
   // private Stack<VideoRenderer.Callbacks> availableRemoteRenderers = new Stack<>();
    private int groupVideoCallRoomID;
    private BigInteger myFeedID;
    private HashMap<Integer, Object> groupVideoCallUsers;
    void setSelcted (VBox tagrget){



        vBoxes.forEach(vBox -> {
            if (vBox.equals(tagrget)){
                vBox.setStyle("-fx-background-color:  #424395");
            }else {
                vBox.setStyle("-fx-background-color: transparent");
            }
        });
        //tagrget.setStyle("-fx-border-color: red");
    }

    JanusPluginHandle pluginHandle;

    private void createRenderer(){
        VideoView videoView = new VideoView();
        videoView.resize(640, 480);
        remoteRenders.add(videoView);
         videoView = new VideoView();
        videoView.resize(640, 480);
        remoteRenders.add(videoView);
        videoView = new VideoView();
        videoView.resize(640, 480);
        remoteRenders.add(videoView);


        videoView = new VideoView();
        videoView.resize(640, 480);
        localRender= videoView;

        /*
            렌더러는 VideoRendererGui 에서 생성되고 관리되는데, create 할 경우 리턴값으로 생성된 렌더러에 대한 참조를 얻을 수 있다.
            렌더링 화면이 겹칠 경우 나중에 create 된 렌더링이 더 위에서 화면을 그린다.

         */
    }



    void cameraTestLocal() {
        var peerConnectionFactory = new PeerConnectionFactory();
        var videoSource = new VideoDeviceSource();
        VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
        videoSource.setVideoCaptureDevice(device);
        videoSource.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0)); //I believe index 0 is auto-resolution, 17 is 1280x720 @ 10fps

        var videoTrack = peerConnectionFactory.createVideoTrack("CAM", videoSource);

        VideoView videoView = new VideoView();
        videoView.resize(640, 480);
        videoTrack.addSink(videoView::setVideoFrame);

        VideoView videoView1 = new VideoView();
        videoView1.resize(640, 480);
        videoTrack.addSink(videoView1::setVideoFrame);

        sideProfileVBox.getChildren().add(videoView);
        sideProfileVBox.getChildren().add(videoView1);

        try {
            videoSource.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            // mediaUtils.stopIncomingCallAlert();

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
