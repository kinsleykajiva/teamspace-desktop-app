package team.space.database.objectio;

import io.objectbox.BoxStore;
import io.objectbox.DebugFlags;
import team.space.MyObjectBox;

public class DBObjectManager {

    public static DBObjectManager  objectIOAccessInstance ;
    private BoxStore store;


    private DBObjectManager() {

         store = MyObjectBox.builder()
                 .name("objectbox-db")
                .debugFlags(DebugFlags.LOG_QUERIES | DebugFlags.LOG_QUERY_PARAMETERS)
                .build();
    }

    public static DBObjectManager getinstance() {
        if (objectIOAccessInstance == null) {
            objectIOAccessInstance = new DBObjectManager();
        }
        return objectIOAccessInstance;
    }

    public BoxStore getStore() {
        return store;
    }
}
