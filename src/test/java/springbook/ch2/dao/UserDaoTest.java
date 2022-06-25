package springbook.ch2.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.ch2.user.dao.UserDao;
import springbook.ch2.user.domain.User;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContextV2.xml")
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @After
    public void after() throws SQLException {
        userDao.deleteAll();
    }

    @Test
    public void addAndGet() throws SQLException {
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
    public void count() throws SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(new User("kyh1", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(new User("kyh2", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(2);
    }

    @Test
    public void getUserFailure() {
        assertThatThrownBy(() -> userDao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}