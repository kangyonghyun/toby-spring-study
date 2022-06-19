package springbook.ch1.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.CountingConnectionMaker;
import springbook.ch1.user.connection.DConnectionMaker;
import springbook.ch1.user.connection.DriverManagerConnectionMaker;
import springbook.ch1.user.dao.CountingDaoFactory;
import springbook.ch1.user.dao.DaoFactory;
import springbook.ch1.user.dao.UserDao;
import springbook.ch1.user.domain.User;

import java.sql.SQLException;

public class MainTest {

    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        User user = new User();
        user.setId("kyh20");
        user.setName("yong");
        user.setPassword("test");

        userDao.add(user);
        log.info("USER 등록 성공 = {}", user.getId());

        User findUser = userDao.get(user.getId());
        log.info("USER 조회 성공 = {}", findUser.getId());

        CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);
        log.info("DB 연결 횟수 = {}", connectionMaker.getCounter());
    }
}
