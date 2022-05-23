package team.space.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import team.space.models.Contact;
import team.space.pojo.UserPojo;
import team.space.requests.getallusers.UsersRoot;
import team.space.requests.login.LoginRoot;
import team.space.requests.resigster.RegisterRoot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static team.space.network.HandleRequests.getHttpClient;
import static team.space.network.HandleRequests.getHttpClientCache;
import static team.space.network.ReqLogin.JSON;
import static team.space.utils.Constants.API_BASE_URL;

public class ReqChats {

    public static void sendChatMessage(String message, String messageType, String toUserId) {




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("messageType", messageType);
            jsonObject.put("toUserId", toUserId);

            OkHttpClient client = getHttpClientCache();
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(API_BASE_URL + "/auth/api/v1/users/chats/chat/save-message")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println("Response: " + res);


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }


public static List<UserPojo> getAllUsersInCompany(){

    OkHttpClient client = getHttpClientCache();

    Request request = new Request.Builder()
            .url("http://localhost:8050/auth/api/v1/users/chats/company-all-users")
//                    .url(API_BASE_URL + "/auth/api/v1/users/login")

            .build();
    String res = "";
    try {
        Response response = client.newCall(request).execute();
        res = response.body().string();
        ObjectMapper om = new ObjectMapper();
        UsersRoot root = om.readValue(res, UsersRoot.class);

        System.out.println("root:: " + root.isSuccess());
        List<UserPojo> userList = new ArrayList<>();

        if(root.isSuccess()){
            root.getData().getUsers().forEach(u->{
                var obj = new UserPojo();
                obj.setActive(u.isActive());
                obj.setCompanyClientId(u.getCompanyClientId());
                obj. setCreatedAt (String.valueOf(u.getCreatedAt().getEpoch_time()) );
                obj.setEmail(u.getEmail());
                obj.setFullName(u.getFullName());
                obj.setId_(u.getId_());
                obj.setProfilePictureUrl(u.getProfilePictureUrl());
                userList.add(obj);
            });

        }
        return userList ;


    } catch (IOException e) {
        e.printStackTrace();
    }
    return Collections.emptyList();

}


public static ObservableList<Contact> getAllUsersCompany(){

    OkHttpClient client = getHttpClientCache();

    Request request = new Request.Builder()
            .url("http://localhost:8050/auth/api/v1/users/chats/company-all-users")
//                    .url(API_BASE_URL + "/auth/api/v1/users/login")

            .build();
    String res = "";
    try {
        Response response = client.newCall(request).execute();
        res = response.body().string();
        System.out.println("Request res: " + res);
        ObjectMapper om = new ObjectMapper();
        UsersRoot root = om.readValue(res, UsersRoot.class);

        System.out.println("root:: " + root.isSuccess());
        ObservableList<Contact>  userList = FXCollections.observableArrayList();

        if(root.isSuccess()){
            root.getData().getUsers().forEach(u->{
                var obj = new Contact(u.getId_() , u.getFullName() , u.getEmail() );
               /* obj.setActive(u.isActive());
                obj.setCompanyClientId(u.getCompanyClientId());
                obj. setCreatedAt (String.valueOf(u.getCreatedAt().getEpoch_time()) );
                obj.setEmail(u.getEmail());
                obj.setFullName(u.getFullName());
                obj.setId_(u.getId_());
                obj.setProfilePictureUrl(u.getProfilePictureUrl());*/
                userList.add(obj);
            });

        }
        return userList ;


    } catch (IOException e) {
        e.printStackTrace();
    }
    return FXCollections.observableArrayList();

}



}
