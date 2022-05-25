package team.space.dto;


import com.j256.ormlite.dao.Dao;
import team.space.database.objectio.LoginInCache;

import java.sql.SQLException;

// AccountDao
public interface AccountDao extends Dao<LoginInCache, String> {

    LoginInCache queryForUsername(String username) throws SQLException;
}
