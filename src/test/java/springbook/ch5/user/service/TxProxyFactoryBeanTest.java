package springbook.ch5.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;

import java.lang.reflect.Proxy;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springbook.ch5.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch5.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/factoryBeanContext.xml")
class TxProxyFactoryBeanTest {

    @Qualifier("userServiceTx")
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @BeforeEach
    void setUp() {
        User user1 = new User("kyh1", "yong", "test", Level.BASIC, MIN_LOGIN_FOR_SILVER,30);
        user1.setEmail("hmoon826@naver.com");
        User user2 = new User("kyh2", "yong", "test", Level.BASIC, MIN_LOGIN_FOR_SILVER - 1,30);
        User user3 = new User("kyh3", "yong", "test", Level.SILVER, 55, MIN_RECOMMEND_FOR_GOLD);
        user3.setEmail("hmoon826@naver.com");
        User user4 = new User("kyh4", "yong", "test", Level.SILVER, 1, MIN_RECOMMEND_FOR_GOLD - 1);
        User user5 = new User("kyh5", "yong", "test", Level.GOLD, 100, 50);
        users = List.of(user1, user2, user3, user4, user5);
    }

    @AfterEach
    void after() {
        userDao.deleteAll();
    }

    @Test
    void upgradeAllOrNothing_factoryBean() {
        for (User user : users) {
            userDao.add(user);
        }

        UserServiceImplTest.TxTestUserService testService = new UserServiceImplTest.TxTestUserService(userDao, users.get(2).getId());
        testService.setMailSender(mailSender);

        UserService service = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                new TransactionHandler(testService, transactionManager, "upgradeLevel"));

        assertThatThrownBy(service::upgradeLevels)
                .isInstanceOf(UserServiceImplTest.TxUserServiceException.class);

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

    static class TxTestUserService extends UserServiceImpl {

        private String id;

        public TxTestUserService(UserDao userDao, String id) {
            super(userDao);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new UserServiceImplTest.TxUserServiceException("강제 예외");
            }
            super.upgradeLevel(user);
        }
    }

    static class TxUserServiceException extends RuntimeException {
        public TxUserServiceException(String message) {
            super(message);
        }
    }

}