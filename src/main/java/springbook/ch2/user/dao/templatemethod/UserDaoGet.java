package springbook.ch2.user.dao.templatemethod;

import springbook.ch2.user.dao.templatemethod.AbstractUserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoGet extends AbstractUserDao {
    @Override
    protected PreparedStatement makeStatement(Connection con) throws SQLException {
        return con.prepareStatement("select * from users where id = ?");
    }
}
