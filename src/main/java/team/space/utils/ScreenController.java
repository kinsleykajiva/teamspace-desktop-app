package team.space.utils;

import java.io.InputStream;
import java.net.URL;

public class ScreenController {


    public static void changeScreen(Screen screen) {
        switch (screen) {

            case WELCOME:
                Shared.screen= Screen.WELCOME;
                StageManager.changeScreen();
                break;
            case MAIN_SCREEN:
                Shared.screen= Screen.MAIN_SCREEN;
                StageManager.changeScreen();
                break;
        }
    }
    public static URL loadURL(String path) {
        return ScreenController.class.getResource(path);
    }


    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return ScreenController.class.getResourceAsStream(name);
    }


    public static String getScreen(Screen screen) {
        switch (screen) {
            case SPLASH_SCREEN: return "/views/splashscreen.fxml";
            case LOGIN: return "/views/login/Main.fxml";
            case MAIN_SCREEN: return "/views/main_default.fxml";

            default:
                return "/views/login.fxml";
        }
    }
}
