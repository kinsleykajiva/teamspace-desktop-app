package team.space.database.sqlite;

import org.sqlite.SQLiteConfig;
import org.sqlite.mc.SQLiteMCConfig;
import team.space.database.objectio.LoginInCache;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBManager {
    private Connection connection;
    public static DBManager dbIsntance;
    /** This executor does the commit job. */
    private final ExecutorService commitExecutor = Executors.newSingleThreadExecutor();
    final String  KEY = "";
    public DBManager() {

        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setSharedCache(true);

            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + PathToUserDocumentsFolder() + "teamspace.db"/*, new SQLiteMCConfig().withKey(KEY).toProperties()*/);
            createTables();
        } catch (final SQLException ex) {
            System.err.println("Error connecting to database: " + ex.getMessage());
        }
    }

    public static DBManager getinstance() {
        if (dbIsntance == null) {
            dbIsntance =  new DBManager();
        }
        return dbIsntance;
    }




    public void saveLoginInCache(LoginInCache cache){

        //  TRUNCATE  table and save new user
        try (Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.execute("DELETE FROM LoginInCache  ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO LoginInCache (companyId, profileImage, fullName, email, userId, role, refreshToken, accessToken) VALUES (?,?,?,?,?,?,?,?)")) {
            statement.setString(1, cache.getCompanyId());
            statement.setString(2, cache.getProfileImage());
            statement.setString(3, cache.getFullName());
            statement.setString(4, cache.getEmail());
            statement.setString(5, cache.getUserId());
            statement.setInt(6, cache.getRole());
            statement.setString(7, cache.getRefreshToken());
            statement.setString(8, cache.getAccessToken());
            statement.executeUpdate();
        } catch (final SQLException ex) {
            System.err.println("Error connecting to database: " + ex.getMessage());
        }

    }
    public LoginInCache getCachedUser(){

        LoginInCache cache = new LoginInCache();
        try (Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT * FROM LoginInCache");
            while (rs.next()) {
                cache.setCompanyId(rs.getString("companyId"));
                cache.setProfileImage(rs.getString("profileImage"));
                cache.setFullName(rs.getString("fullName"));
                cache.setEmail(rs.getString("email"));
                cache.setUserId(rs.getString("userId"));
                cache.setRole(rs.getInt("role"));
                cache.setRefreshToken(rs.getString("refreshToken"));
                cache.setAccessToken(rs.getString("accessToken"));
            }
        } catch (final SQLException ex) {
            System.err.println("Error connecting to database: " + ex.getMessage());
        }
        return cache;
    }


    private String PathToUserDocumentsFolder() {
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + DATABASE_FOLDER_NAME;
        File customDir = new File(path);
        if (customDir.exists()) {
            System.out.println(customDir + " already exists");
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
        } else {
            System.out.println(customDir + " was not created");
        }
        return path + File.separator;
    }
    private void createTables() {
        var loginincacheTable = """
                CREATE TABLE IF NOT EXISTS LoginInCache (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER,
                    role INTEGER,
                    accessToken TEXT,
                    refreshToken TEXT,
                    profileImage TEXT,
                    fullName TEXT,
                    email TEXT,
                    companyId TEXT,
                    UNIQUE(email,accessToken)
                );
                    """;



        List<String> tables = List.of(loginincacheTable);
        tables.forEach(table -> {
            try (Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                statement.execute(table);

                System.out.println("Tables created");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }


    /**
     * @return Database folder name <b>with out</b> separator [example:XR3DataBase]
     */
    public static String getDatabaseFolderName() {
        return DATABASE_FOLDER_NAME;
    }

    private static final String DATABASE_FOLDER_NAME = "teamspace";

}
