package team.space.controllers.filesmanager.constants;

import javafx.scene.Scene;
import javafx.stage.Stage;
import team.space.controllers.filesmanager.FileManagerViewController;
import team.space.controllers.filesmanager.model.FileDetail;


public class CommonData {
    public static FileType CURRENT_LIST_VIEW_ITEM = FileType.UNKNOWN;
    public static FileManagerViewController instance = null;
    public static Scene CONTROLLER_SCENE = null;
    public static LogicConstants.OS OS = LogicConstants.OS.UNKNOWN;
    public static FileDetail CURRENT_DIRECTORY = null;

    public static String VIEW_MODE = "LISTVIEW";

    public static Stage transferStage = null;
}
