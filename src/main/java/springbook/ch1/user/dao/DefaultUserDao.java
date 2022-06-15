package springbook.ch1.user.dao;

import springbook.ch1.user.connection.ConnectionConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DefaultUserDao extends AbstractUserDao {
    @Override
    public Connection getConnection() throws SQLException {
        // 기본 커넥션 획득 방법 - DriverManager
        return DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
    }
}
