package team.space.database.objectio;

import io.objectbox.BoxStore;
import io.objectbox.DebugFlags;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;

public class DBObjectManager {

    public static DBObjectManager  objectIOAccessInstance ;
    private BoxStore store;


    private DBObjectManager() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("objectdb", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempFile.delete();
         store = MyObjectBox.builder()
                .directory(tempFile)
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
