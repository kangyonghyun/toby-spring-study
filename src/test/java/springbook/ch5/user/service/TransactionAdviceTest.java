package springbook.ch5.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;
import springbook.ch5.user.service.testservice.TxTestUserService;
import springbook.ch5.user.service.testservice.TxUserServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static springbook.ch5.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch5.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/aopContext.xml")
class TransactionAdviceTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao userDao;

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
        for (User user : users) {
            userDao.add(user);
        }
    }

    @AfterEach
    void after() {
        userDao.deleteAll();
    }

    @Test
    @DisplayName("advisor 을 적용한 트랜잭션 검증")
    void upgradeAllOrNothing_advice() {
        TxTestUserService testService = new TxTestUserService(userDao, users.get(2).getId());
        testService.setMailSender(mailSender);

        ProxyFactoryBean proxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        proxyFactoryBean.setTarget(testService);
        UserService service = (UserService) proxyFactoryBean.getObject();

        assertThatThrownBy(service::upgradeLevels)
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