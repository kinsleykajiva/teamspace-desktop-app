package team.space.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import team.space.models.Contact;
import team.space.requests.files.getfiles.FileObject;
import team.space.requests.getallusers.UsersRoot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team.space.network.HandleRequests.getHttpClient;
import static team.space.network.HandleRequests.getHttpClientCache;

public class ReqFilesHandler {

    private static Map<String, List<FileObject>> filesMap = null;

    public static Map<String, List<FileObject>>  getFilesMap(){
        if(filesMap == null){
            filesMap = new HashMap<>();
        }
        return filesMap;
    }





    public static List<team.space.requests.files.getfiles.FileObject> listMyFiles() {

        List<team.space.requests.files.getfiles.FileObject> filesList = new ArrayList<>();


        Request request = new Request.Builder()
                .url("http://localhost:5912/files/?createdByUserId=sdsdsd")
                .build();

        try {
            Response response = getHttpClient().newCall(request).execute();
          String  res = response.body().string();
            System.out.println("XXXRequest res: " + res);
            ObjectMapper om = new ObjectMapper();
            team.space.requests.files.getfiles.GetFilesRoot root = om.readValue(res, team.space.requests.files.getfiles.GetFilesRoot.class);
            // filesList = root.getData().getFileObjects();
            if(root.getSuccess()){
                filesList.addAll(root.getData().getFileObjects());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return filesList;
    }

}
