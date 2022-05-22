package team.space.database.rethinkdb;


import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

public class DBManager {
    private static DBManager dbIsntance;
    public static final RethinkDB r = RethinkDB.r;
    private final Connection conn;

    private DBManager() {

        conn = r.connection()
                .hostname("13.246.49.140")
                .port(28015)
                .db("teamspace")
                .connect();


    }

    public static DBManager getinstance() {
        if (dbIsntance == null) {
            dbIsntance =  new DBManager();
        }

       return dbIsntance;

    }

    public Connection getConn() {
        return conn;
    }
}
