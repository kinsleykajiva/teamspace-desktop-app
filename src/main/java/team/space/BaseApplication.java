package team.space;


import com.dustinredmond.fxtrayicon.FXTrayIcon;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import team.space.database.objectio.DBObjectManager;
import team.space.database.sqlite.DBManager;
import team.space.events.MessageEvent;
import team.space.utils.Shared;
import team.space.utils.StageManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import static team.space.controllers.LoginInController.startMainView;
import static team.space.database.sqlite.DBManager.createTables2;
import static team.space.webrtc.webrtcutils.WebRTCUtils.initWebRTC;
import static team.space.webrtc.webrtcutils.WebRTCUtils.isMediaReady;

public class BaseApplication extends Application {
    DBManager sqlAccess;

    @Override
    public void stop() throws Exception {
        super.stop();
        EventBus.getDefault().unregister(this);
    }

    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {


        System.out.println("Received event: " + event.getEventType());
        // textField.setText(event.message);
    }

    @Override
    public void start(Stage stage) throws IOException {

        DBObjectManager.getinstance();
        DBManager.getinstance();
        sqlAccess = new DBManager();
        EventBus.getDefault().register(this);


        Shared.LOGGED_USER = DBManager.getinstance().getCachedUser();
// Pass in the app's main stage, and path to the icon image
        FXTrayIcon icon = new FXTrayIcon(stage, getClass().getResource("/images/group-chat.png"));
        icon.show();
        System.out.println("LoginInCache not exists" +  Shared.LOGGED_USER);
        if (Shared.LOGGED_USER == null) {
            // ask the user to login

            StageManager.setStage(stage);

            Parent root = FXMLLoader.load(getClass().getResource("/views/login/Main.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/group-chat.png")));
            stage.show();
        } else {
            //   show them the home page
            startMainView(getClass());
        }

        Platform.runLater(() -> {
            MFXNotificationSystem.instance().initOwner(stage);
            MFXNotificationCenterSystem.instance().initOwner(stage);

            MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
            center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
                {
                    setPrefHeight(100);
                }
            });
           // initWebRTC();
          //  isMediaReady();
        });



    }
    public static void main(String[] args) throws SQLException {
        Locale.setDefault(Locale.ENGLISH);
        createTables2();



        launch();
    }
}
