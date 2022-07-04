package springbook.ch3.user.dao;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import springbook.ch3.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@ContextConfiguration("/jdbctemplateV2.xml")
public class UserDaoImplTest {

    @Autowired
    UserDao userdao;

    @Autowired
    DataSource dataSource;

//    @Configuration
//    static class config {
//        @Bean
//        UserDao userDao() {
//            UserDao userDao = new UserDaoImpl(dataSource());
//            return userDao;
//        }
//
//        @Bean
//        public DataSource dataSource() {
//            DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/tobytest",
//                    "sa", "", true);
//            return dataSource;
//        }
//    }

    @After
    public void end() throws SQLException {
        userdao.deleteAll();
    }

    @Test
    public void get(){
        User user1 = new User("kyh1", "yong", "test");
        User user2 = new User("kyh2", "yong", "test");
        userdao.add(user1);
        userdao.add(user2);

        assertThat(userdao.get("kyh1")).isEqualTo(user1);
    }

    @Test
    public void count() {
        assertThat(userdao.getCount()).isEqualTo(0);
        userdao.add(new User("kyh1", "yong", "test"));
        userdao.add(new User("kyh2", "yong", "test"));
        assertThat(userdao.getCount()).isEqualTo(2);
        userdao.deleteAll();
        assertThat(userdao.getCount()).isEqualTo(0);
    }

    @Test
    public void getAll() throws SQLException {
        User user1 = new User("kyh2", "yong", "test");
        userdao.add(user1);
        List<User> users1 = userdao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(users1.get(0), user1);

        User user2 = new User("kyh1", "yong", "test");
        userdao.add(user2);
        List<User> users2 = userdao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(users2.get(0), user2);
        checkSameUser(users2.get(1), user1);
    }

    @Test
    public void getAll_zero() {
        List<User> users = userdao.getAll();
        assertThat(users).size().isEqualTo(0);
    }

    private void checkSameUser(User actual, User expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getUserFailure() {
        assertThatThrownBy(() -> userdao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void duplicateKey() {
        User user1 = new User("kyh2", "yong", "test");
        User user2 = new User("kyh2", "yong", "test");
        userdao.add(user1);
        assertThatThrownBy(() -> userdao.add(user2))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    public void sqlExceptionTranslate() {
        User user1 = new User("kyh2", "yong", "test");
        User user2 = new User("kyh2", "yong", "test");
        try {
            userdao.add(user1);
            userdao.add(user2);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLErrorCodeSQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            DataAccessException dae = set.translate(null, null, sqlEx);
            assertThat(dae).isInstanceOf(DuplicateKeyException.class);
        }
    }

}