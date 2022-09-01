package springbook.ch6.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.ch6.user.config.TestApplicationContext;
import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.domain.Level;
import springbook.ch6.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springbook.ch6.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch6.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplicationContext.class)
class UserServiceImplTest {

    @Qualifier("testUserService")
    @Autowired
    UserService testService;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    void setUp() {
        User user1 = new User("kyh1", "yong", "test", Level.BASIC, MIN_LOGIN_FOR_SILVER,30);
        user1.setEmail("hmoon826@naver.com");
        User user2 = new User("kyh2", "yong", "test", Level.BASIC, MIN_LOGIN_FOR_SILVER - 1,30);
        User user3 = new User("kyh3_ex", "yong", "test", Level.SILVER, 55, MIN_RECOMMEND_FOR_GOLD);
        user3.setEmail("hmoon826@naver.com");
        User user4 = new User("kyh4", "yong", "test", Level.SILVER, 1, MIN_RECOMMEND_FOR_GOLD - 1);
        User user5 = new User("kyh5", "yong", "test", Level.GOLD, 100, 50);
        users = List.of(user1, user2, user3, user4, user5);
        for (User user : users) {
            testService.save(user);
        }
    }

    @AfterEach
    void after() {
        userDao.deleteAll();
    }

    @Test
    void save() {
        User user1 = new User("kyh7", "yong", "ps", Level.GOLD, 0, 0);
        User user2 = new User("kyh8", "yong", "ps");
        testService.save(user1);
        testService.save(user2);

        assertThat(user1.getLevel()).isEqualTo(Level.GOLD);
        assertThat(user2.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeLevels() {
        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), true);
        checkLevelUpgraded(users.get(1), false);
        checkLevelUpgraded(users.get(2), true);
        checkLevelUpgraded(users.get(3), false);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void upgradeAllOrNothing() {
        assertThatThrownBy(testService::upgradeLevels)
                .isInstanceOf(TxUserServiceException.class);

        checkLevelUpgraded(users.get(0), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User findUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(findUser.getLevel()).isEqualTo(user.getLevel());
        }
    }
}