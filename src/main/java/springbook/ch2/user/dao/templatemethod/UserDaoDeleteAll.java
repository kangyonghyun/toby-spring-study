package springbook.ch2.user.dao.templatemethod;

import springbook.ch2.user.dao.templatemethod.AbstractUserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll extends AbstractUserDao {
    @Override
    protected PreparedStatement makeStatement(Connection con) throws SQLException {
        return con.prepareStatement("delete from users");
    }
}
