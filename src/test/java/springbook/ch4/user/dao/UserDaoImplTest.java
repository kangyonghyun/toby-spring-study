package springbook.ch4.user.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import springbook.ch4.JavaConfig;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/jdbctemplateV3.xml")
//@SpringJUnitConfig(JavaConfig.class)
class UserDaoImplTest {

    @Autowired
    UserDao userdao;

    @Autowired
    DataSource dataSource;

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {
        this.user1 = new User("kyh3", "yong", "test", Level.BASIC, 1,0);
        this.user2 = new User("kyh1", "yong", "test", Level.SILVER, 55, 10);
        this.user3 = new User("kyh2", "yong", "test", Level.GOLD, 100, 40);
        userdao.add(user1);
        userdao.add(user2);
        userdao.add(user3);
    }

    @AfterEach
    void after() {
        userdao.deleteAll();
    }

    @Test
    void get(){
        assertThat(userdao.get("kyh3")).isEqualTo(user1);
    }

    @Test
    void count() {
        assertThat(userdao.getCount()).isEqualTo(3);
    }

    @Test
    void getAll() throws SQLException {
        List<User> users1 = userdao.getAll();
        checkSameUser(users1.get(2), user1);
        checkSameUser(users1.get(0), user2);
        checkSameUser(users1.get(1), user3);
    }

    @Test
    void getAll_zero() {
        userdao.deleteAll();
        List<User> users = userdao.getAll();
        assertThat(users).size().isEqualTo(0);
    }

    private void checkSameUser(User actual, User expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getLevel()).isEqualTo(expected.getLevel());
        assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
        assertThat(actual.getRecommend()).isEqualTo(expected.getRecommend());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserFailure() {
        assertThatThrownBy(() -> userdao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void duplicateKey() {
        User user4 = new User("kyh3", "yong", "test", Level.BASIC, 0, 0);
        assertThatThrownBy(() -> userdao.add(user4))
                .isInstanceOf(DuplicateKeyException.class);
    }

}