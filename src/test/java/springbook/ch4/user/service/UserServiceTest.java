package springbook.ch4.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/jdbctemplateV3.xml")
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    void setUp() {
        User user1 = new User("kyh1", "yong", "test", Level.BASIC, 50,30);
        User user2 = new User("kyh2", "yong", "test", Level.BASIC, 49,30);
        User user3 = new User("kyh3", "yong", "test", Level.SILVER, 55, 30);
        User user4 = new User("kyh4", "yong", "test", Level.SILVER, 1, 29);
        User user5 = new User("kyh5", "yong", "test", Level.GOLD, 100, 50);
        users = List.of(user1, user2, user3, user4, user5);
    }

    @AfterEach
    void after() {
        userDao.deleteAll();
    }

    @Test
    void save() {
        User user1 = new User("kyh1", "yong", "ps", Level.GOLD, 0, 0);
        User user2 = new User("kyh2", "yong", "ps");
        userService.save(user1);
        userService.save(user2);

        assertThat(user1.getLevel()).isEqualTo(Level.GOLD);
        assertThat(user2.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeLevels() {
        for (User user : users) {
            userDao.add(user);
        }
        userService.upgradeLevels();

        checkLevel(users.get(0), Level.SILVER);
        checkLevel(users.get(1), Level.BASIC);
        checkLevel(users.get(2), Level.GOLD);
        checkLevel(users.get(3), Level.SILVER);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User findUser = userDao.get(user.getId());
        assertThat(findUser.getLevel()).isEqualTo(level);
    }

}