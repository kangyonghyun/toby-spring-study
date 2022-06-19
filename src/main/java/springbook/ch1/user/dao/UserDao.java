package springbook.ch1.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.DConnectionMaker;
import springbook.ch1.user.connection.SimpleConnectionMaker;
import springbook.ch1.user.domain.User;
import springbook.ch1.user.connection.ConnectionConst;

import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UserDao {

    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private final ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws SQLException {
        Connection con = connectionMaker.makeConnection();
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
        Connection con = connectionMaker.makeConnection();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDao userDao = (UserDao) o;
        return Objects.equals(connectionMaker, userDao.connectionMaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionMaker);
    }
}
