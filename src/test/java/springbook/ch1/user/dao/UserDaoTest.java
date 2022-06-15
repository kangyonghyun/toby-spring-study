package springbook.ch1.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import springbook.ch1.user.domain.User;
import springbook.ch1.user.connection.ConnectionConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserDaoTest {

    UserDao userDao = new UserDao();

    @Test
    void connection() throws SQLException {
        Connection con = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        log.info("connection = {}, class = {}", con, con.getClass());
        assertThat(con).isNotNull();
    }

    @Test
    void crud() throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setId("kyh7");
        user.setName("yong");
        user.setPassword("test");
        userDao.add(user);

        User findUser = userDao.get(user.getId());
        assertThat(findUser).isEqualTo(user);
    }

}