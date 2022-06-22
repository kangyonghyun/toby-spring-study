package springbook.ch1.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.DriverManagerConnectionMaker;
import springbook.ch1.user.domain.User;
import springbook.ch1.user.connection.ConnectionConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class UserDaoTest {
    SimpleDriverDataSource dataSource;
    ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

    @BeforeEach
    void before() {
        dataSource = new SimpleDriverDataSource();
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
    void addAndGet() throws SQLException {

        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user1 = new User("kyh1", "yong", "test");
        userDao.add(user1);

        User user2 = new User("kyh2", "yong", "test");
        userDao.add(user2);

        User findUser1 = userDao.get(user1.getId());
        assertThat(findUser1).isEqualTo(user1);

        User findUser2 = userDao.get(user2.getId());
        assertThat(findUser2).isEqualTo(user2);

        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.deleteAll();
    }

    @Test
    void count() throws SQLException {
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(new User("kyh1", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(new User("kyh2", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(2);
    }

    @Test
    void getUserFailure() throws SQLException {
        UserDao userDao = context.getBean("userDao", UserDao.class);

        assertThatThrownBy(() -> userDao.get("kyh1"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}