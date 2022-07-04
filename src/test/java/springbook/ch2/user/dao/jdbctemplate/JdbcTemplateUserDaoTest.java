package springbook.ch2.user.dao.jdbctemplate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import springbook.ch2.JavaConfig;
import springbook.ch2.user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/jdbctemplate.xml")
@SpringJUnitConfig(JavaConfig.class)
public class JdbcTemplateUserDaoTest {

    @Autowired
    JdbcTemplateUserDao userdao;

    @AfterEach
    void end() throws SQLException {
        userdao.deleteAll();
    }

    @Test
    void get(){
        User user1 = new User("kyh1", "yong", "test");
        User user2 = new User("kyh2", "yong", "test");
        userdao.add(user1);
        userdao.add(user2);

        assertThat(userdao.get("kyh1")).isEqualTo(user1);
    }

    @Test
    void count() {
        assertThat(userdao.getCount()).isEqualTo(0);
        userdao.add(new User("kyh1", "yong", "test"));
        userdao.add(new User("kyh2", "yong", "test"));
        assertThat(userdao.getCount()).isEqualTo(2);
        userdao.deleteAll();
        assertThat(userdao.getCount()).isEqualTo(0);
    }

    @Test
    void getAll() throws SQLException {
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
    void getAll_zero() {
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
     void getUserFailure() {
        assertThatThrownBy(() -> userdao.get("kyh"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

}