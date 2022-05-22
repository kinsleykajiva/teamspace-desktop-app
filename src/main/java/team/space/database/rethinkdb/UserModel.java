package team.space.database.rethinkdb;

//import com.rethinkdb.net.Result;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.time.OffsetDateTime;

import static com.rethinkdb.gen.proto.TermType.JSON;
import static team.space.database.rethinkdb.DBManager.getinstance;
import static team.space.database.rethinkdb.DBManager.r;
import com.rethinkdb.net.Cursor;
import team.space.pojo.UserPojo;
import team.space.pojo.UserPojo2;

public class UserModel {



    public static final void saveUser(String email,String password,String fullName , String profilePictureUrl ,
                                         boolean isActive,boolean isIsVerified, String companyClientId) {
        OffsetDateTime myDateTime = OffsetDateTime.now();
        r.table("users")
                .insert(
                        r.hashMap("email", email)
                                .with("password", password)
                                .with("fullName", fullName)
                                .with("profilePictureUrl", profilePictureUrl)
                                .with("isActive", isActive)
                                .with("companyClientId", companyClientId)
                                .with("isIsVerified", isIsVerified)
                                .with("createdAt", myDateTime)
                ).run(getinstance().getConn());
    }

    public static void getAllUsers(){
       /* Cursor changeCursor = r.table("users").changes().run(conn);
        for (Object change : changeCursor) {
            System.out.println(change);
        }*/


       /* try (Result<Object> result = r.table("users").run(getinstance().getConn())) {

            result.forEach(doc -> {

                System.out.println((doc.toString()));
             //   System.out.println(new JSONObject(doc.toString()));
            });
          //  Object doc = result.next();
        }*/

      var resultz =   r.table("users")/*.filter(
                row -> row.g("id").eq("1c1280d6-efa4-433d-b695-af3e1afaee9f")
        )*/.changes().run(getinstance().getConn());

     //   resultz.forEach(doc -> { System.err.println(doc); });
        Cursor<UserPojo2> cursor =  r.table("users").run(getinstance().getConn(), UserPojo2.class);

        for (UserPojo2 obj : cursor) {
           /* Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(obj.toString());*/
            var s = "{profilePictureUrl=null, createdAt=2022-05-20T08:57:29.083Z, password=12345, companyClientId=a146fe34-c3e1-43b9-8d2e-3abf2ad4dbb9, fullName=Wanda111, id=49c3459b-63a5-473d-b77d-2f8a4f048fd8, isActive=true, email=wandt23yutyta111@shoprite.com, isIsVerified=true}";
            System.out.println(obj.getFullName());

        }
         //   System.out.println(getObject(obj.toString() , UserPojo.class));


      //      JsonObject jsonObjectAlt = JsonParser.parseString(obj.toString()).getAsJsonObjec
}
}
