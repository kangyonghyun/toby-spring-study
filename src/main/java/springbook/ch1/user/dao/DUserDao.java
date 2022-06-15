package springbook.ch1.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DUserDao extends AbstractUserDao {
    @Override
    public Connection getConnection() throws SQLException {
        // D 사 커넥션 획득 방법
        return null;
    }
}
