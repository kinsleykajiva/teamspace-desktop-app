package team.space.database.rethinkdb;

import java.time.OffsetDateTime;

import static team.space.database.rethinkdb.DBManager.getinstance;
import static team.space.database.rethinkdb.DBManager.r;

public class TeamModel {


    public static final void saveTeam(String title, String  companyClientId , boolean isActive ) {
        OffsetDateTime myDateTime = OffsetDateTime.now();
        r.table("teams")
                .insert(
                        r.hashMap("title", title)

                                .with("isActive", isActive)
                                .with("companyClientId", companyClientId)

                                .with("createdAt", myDateTime)
                ).run(getinstance().getConn());
    }



}
