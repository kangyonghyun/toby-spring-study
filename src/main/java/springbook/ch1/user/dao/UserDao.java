package springbook.ch1.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbook.ch1.user.domain.User;
import springbook.connection.ConnectionConst;

import java.sql.*;
import java.util.NoSuchElementException;

public class UserDao {

    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setId("kyh12");
        user.setName("yong");
        user.setPassword("test");

        userDao.add(user);
        log.info("USER 등록 성공 = {}", user.getId());

        User findUser = userDao.get(user.getId());
        log.info("USER 조회 성공 = {}", user.getId());

    }

    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection con = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
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
        Connection con = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
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
