package springbook.ch1.user.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbook.ch1.user.domain.User;

import java.sql.SQLException;

public class MainTest {

    static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public static void main(String[] args) throws SQLException {
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
}
