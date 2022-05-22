package team.space.database.rethinkdb;

import java.time.OffsetDateTime;
import java.util.Date;

import static team.space.database.rethinkdb.DBManager.getinstance;
import static team.space.database.rethinkdb.DBManager.r;

public class CompanyModel {


    public static final void saveCompany(String title,String adminEmail) {
        OffsetDateTime myDateTime = OffsetDateTime.now();
        r.table("company")
                .insert(
                        r.hashMap("title", title)
                                .with("adminEmail", adminEmail)
                                .with("isActive", true)
                                .with("createdAt", myDateTime)
        ).run(getinstance().getConn());
    }
}
