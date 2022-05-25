package team.space.dto;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import team.space.database.objectio.LoginInCache;

import java.sql.SQLException;

public class AccountDaoImpl extends BaseDaoImpl<LoginInCache, String> implements AccountDao {
    public AccountDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, LoginInCache.class);
    }

    @Override
    public LoginInCache queryForUsername(String username) throws SQLException {
        return queryForFirst(queryBuilder().where().eq("username", username).prepare());
    }
}

