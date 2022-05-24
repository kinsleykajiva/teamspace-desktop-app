package team.space;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.objectbox.Box;
import io.objectbox.query.Query;
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
import team.space.database.objectio.LoginInCache;
import team.space.database.sqlite.DBManager;
import team.space.events.MessageEvent;
import team.space.utils.Shared;
import team.space.utils.StageManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static team.space.controllers.LoginInController.startMainView;
import static team.space.utils.Constants.QUEUE_ON_USER_SAVED;

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



        Box<LoginInCache> loginInCacheBox = DBObjectManager.getinstance().getStore().boxFor(LoginInCache.class);
        Query<LoginInCache> checkExistsquery1 = loginInCacheBox.query().build();

        if (checkExistsquery1.find().size() > 0) {
            System.out.println("LoginInCache exists --- "  + checkExistsquery1.findFirst());
           // return checkExistsquery1.find().get(0);
        }else{
            System.out.println("LoginInCache not exists");
           // return null;
        }


        Shared.LOGGED_USER = DBManager.getinstance().getCachedUser();

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
        });

    }
}
