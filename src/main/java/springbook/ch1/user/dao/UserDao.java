package springbook.ch1.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbook.ch1.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class UserDao {

    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("connection = {}", con.getClass());

        PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public User get(String id) throws SQLException {
        Connection con = dataSource.getConnection();
        PreparedStatement ps = con.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        User user = new User();
        if (rs.next()) {
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        } else {
            throw new NoSuchElementException();
        }

        rs.close();
        ps.close();
        con.close();

        return user;
    }

}
