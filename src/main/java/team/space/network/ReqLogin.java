package team.space.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.objectbox.Box;
import io.objectbox.query.Query;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import team.space.database.objectio.DBObjectManager;
import team.space.database.objectio.LoginInCache;
import team.space.database.sqlite.DBManager;
import team.space.requests.login.LoginRoot;
import team.space.requests.resigster.RegisterRoot;

import java.io.IOException;
import java.util.Date;

import static team.space.network.HandleRequests.getHttpClient;
import static team.space.network.HandleRequests.getHttpClientCache;
import static team.space.utils.Constants.API_BASE_URL;

public class ReqLogin {
    static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static RegisterRoot registerRequest(String fullName, String companyName, String emailAddress, String password) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fullName", fullName);
            jsonObject.put("companyName", companyName);
            jsonObject.put("emailAddress", emailAddress);
            jsonObject.put("password", password);

            OkHttpClient client = getHttpClientCache();
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(API_BASE_URL + "/auth/api/v1/users/register-company")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println("Response: " + res);
            ObjectMapper om = new ObjectMapper();
            RegisterRoot root = om.readValue(res, RegisterRoot.class);
            return root;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LoginInCache loginReq(String emailAddress, String password) {

        DBManager sqlAccess = new DBManager();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", emailAddress);
            jsonObject.put("password", password);

            OkHttpClient client = getHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

            System.out.println("Request Body: " + API_BASE_URL + "/auth/api/v1/users/login");


            Request request = new Request.Builder()
                    .url("http://localhost:8050/auth/api/v1/users/login")
//                    .url(API_BASE_URL + "/auth/api/v1/users/login")
                    .post(body)
                    .build();
            String res = "";
            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("111res: " + res);
            ObjectMapper om = new ObjectMapper();

            LoginRoot root = om.readValue(res, LoginRoot.class);

            System.out.println("root:: " + root.isSuccess());

            if (root.isSuccess()) {
                Box<LoginInCache> store = DBObjectManager.getinstance().getStore().boxFor(LoginInCache.class);
                store.removeAll(); // clear before insert
                LoginInCache user = new LoginInCache();
                user.setDate(new Date());
                user.setRefreshToken(root.getData().getToken().getRefreshToken());
                user.setProfileImage(root.getData().getProfileImage());
                user.setFullName(root.getData().getFullName());
                user.setEmail(root.getData().getEmail());
                user.setRole(root.getData().getRole());
                user.setCompanyId(root.getData().getCompanyId());
                user.setUserId(root.getData().getUserId());
                user.setAccessToken(root.getData().getToken().getAccessToken());

                store.put(user);
                sqlAccess. saveLoginInCache(user);


                Box<LoginInCache> loginInCacheBox = DBObjectManager.getinstance().getStore().boxFor(LoginInCache.class);
                Query<LoginInCache> checkExistsquery = loginInCacheBox.query().build();

                if (checkExistsquery.find().size() > 0) {
                    System.out.println("LoginInCache exists");
                    return checkExistsquery.find().get(0);
                }else{
                    System.out.println("LoginInCache not exists");
                    return null;
                }
//                return user;

            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return null;}
}
