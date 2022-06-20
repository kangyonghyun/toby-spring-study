package springbook.ch1.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.DriverManagerConnectionMaker;
import springbook.ch1.user.domain.User;
import springbook.ch1.user.connection.ConnectionConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class UserDaoTest {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    UserDao userDao = new UserDao();

    @BeforeEach
    void before() {
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPassword(ConnectionConst.PASSWORD);
    }

    @Test
    void connection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("connection = {}, class = {}", con, con.getClass());
        assertThat(con).isNotNull();
    }

    @Test
    void crud() throws SQLException, ClassNotFoundException {
        userDao.setDataSource(dataSource);

        User user = new User();
        user.setId("kyh11111");
        user.setName("yong");
        user.setPassword("test");
        userDao.add(user);

        User findUser = userDao.get(user.getId());
        assertThat(findUser).isEqualTo(user);
    }

}