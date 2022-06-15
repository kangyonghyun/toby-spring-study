package springbook.ch1.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class NUserDao extends AbstractUserDao {
    @Override
    public Connection getConnection() throws SQLException {
        // N 사 커넥션 획득 방법
        return null;
    }
}
