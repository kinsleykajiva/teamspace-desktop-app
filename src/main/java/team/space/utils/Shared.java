package team.space.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.json.JSONObject;
import team.space.beans.Observable;
import team.space.database.objectio.LoginInCache;
import team.space.models.ChatHistory;
import team.space.models.Contact;
import team.space.pojo.UserPojo;

public class Shared {

    public static String appName;
    public static String appVersion;

    public static LoginInCache LOGGED_USER = null;
    public static JSONObject LOGGED_USER_SESSION_OBJECT =null;

    public static Screen screen;
    public static boolean isLocalHost = true;
    // public static ObservableArrayList<UserPojo>

   public static ObservableList<Contact> contactObservableArrayList =  FXCollections.observableArrayList();
    public  static MutableMap<Contact, ChatHistory> historyMutableMap = Maps.mutable.empty();

   // ObservableMap<Contact, ChatHistory> historyMap2 = FXCollections.observableHashMap();
}
