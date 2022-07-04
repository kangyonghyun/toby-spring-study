package springbook.ch1.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.ch1.user.domain.User;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @AfterEach
     void after() throws SQLException {
        userDao.deleteAll();
    }

    @Test
     void addAndGet() throws SQLException {
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
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(new User("kyh1", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(new User("kyh2", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(2);
    }

    @Test
     void getUserFailure() {
        assertThatThrownBy(() -> userDao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}