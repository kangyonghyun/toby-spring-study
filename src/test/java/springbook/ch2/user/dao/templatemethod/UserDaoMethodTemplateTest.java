package springbook.ch2.user.dao.templatemethod;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.ch2.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UserDaoMethodTemplateTest {

    AbstractUserDao userDao;
    DataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/tobytest", "sa", "", true);
    }

    @After
    public void after() throws SQLException {
        userDao = new UserDaoDeleteAll();
        userDao.setDataSource(dataSource);
        userDao.deleteAll();
    }

    @Test
    public void addAndGet() throws SQLException {
        userDao = new UserDaoAdd();
        userDao.setDataSource(dataSource);

        User user1 = new User("kyh1", "yong", "test");
        userDao.add(user1);

        User user2 = new User("kyh2", "yong", "test");
        userDao.add(user2);

        userDao = new UserDaoGet();
        userDao.setDataSource(dataSource);

        User findUser1 = userDao.get(user1.getId());
        assertThat(findUser1).isEqualTo(user1);

        User findUser2 = userDao.get(user2.getId());
        assertThat(findUser2).isEqualTo(user2);

        assertThat(userDao.getCount()).isEqualTo(2);
    }

    @Test
    public void count() throws SQLException {
        userDao = new UserDaoDeleteAll();
        userDao.setDataSource(dataSource);

        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao = new UserDaoAdd();
        userDao.setDataSource(dataSource);

        userDao.add(new User("kyh1", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(new User("kyh2", "yong", "test"));
        assertThat(userDao.getCount()).isEqualTo(2);
    }

    @Test
    public void getUserFailure() {
        userDao = new UserDaoGet();
        userDao.setDataSource(dataSource);

        assertThatThrownBy(() -> userDao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}