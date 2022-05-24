package team.space.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    private static Stage stage;


    public static void setStage(Stage stage) {
        StageManager.stage = stage;
    }

    public StageManager(Stage stage) {
        this.stage = stage;
        try {
            Parent root = FXMLLoader.load(StageManager.class.getResource(ScreenController.getScreen(Shared.screen)));
            var scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            ResizeHelper.addResizeListener(stage);
            stage.centerOnScreen();
            /*root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
                root.setCursor(Cursor.CLOSED_HAND);
            });
            root.setOnMouseReleased(event -> {
                root.setCursor(Cursor.DEFAULT);
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void changeScreen() {
        try {
            StageManager.stage.getScene().setRoot(FXMLLoader.load(ScreenController.class.getResource(ScreenController.getScreen(Shared.screen))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
