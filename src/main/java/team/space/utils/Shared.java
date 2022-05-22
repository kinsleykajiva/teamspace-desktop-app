package team.space.utils;

import org.json.JSONObject;
import team.space.database.objectio.LoginInCache;

public class Shared {

    public static String appName;
    public static String appVersion;

    public static LoginInCache LOGGED_USER = null;
    public static JSONObject LOGGED_USER_SESSION_OBJECT =null;

    public static Screen screen;
    public static boolean isLocalHost = true;

//    public static ObservableArrayList<Contact> contactObservableArrayList = new ObservableArrayList<>();
//    public  static MutableMap<Contact, ChatHistory> historyMutableMap = Maps.mutable.empty();

   // ObservableMap<Contact, ChatHistory> historyMap2 = FXCollections.observableHashMap();
}
