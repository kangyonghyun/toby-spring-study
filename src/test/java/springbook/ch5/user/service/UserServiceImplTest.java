package springbook.ch5.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static springbook.ch5.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch5.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/jdbctemplateV4.xml")
class UserServiceImplTest {

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
    void save() {
        User user1 = new User("kyh1", "yong", "ps", Level.GOLD, 0, 0);
        User user2 = new User("kyh2", "yong", "ps");
        userService.save(user1);
        userService.save(user2);

        assertThat(user1.getLevel()).isEqualTo(Level.GOLD);
        assertThat(user2.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeLevels() throws Exception {
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        UserServiceImpl service = new UserServiceImpl(userDao);
        service.setMailSender(mockMailSender);

        service.upgradeLevels();

        checkLevelUpgraded(users.get(0), true);
        checkLevelUpgraded(users.get(1), false);
        checkLevelUpgraded(users.get(2), true);
        checkLevelUpgraded(users.get(3), false);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests).size().isEqualTo(2);
        assertThat(requests).containsExactly(users.get(0).getEmail(), users.get(2).getEmail());
    }

    @Test
    void mockUserDaoTest() {
        MockUserDao mockUserDao = new MockUserDao(users);
        MockMailSender mockMailSender = new MockMailSender();

        UserServiceImpl service = new UserServiceImpl(mockUserDao);
        service.setMailSender(mockMailSender);

        service.upgradeLevels();

        List<User> updatedUsers = mockUserDao.getUpdatedUsers();

        checkUserAndLevel(updatedUsers.get(0), "kyh1", Level.SILVER);
        checkUserAndLevel(updatedUsers.get(1), "kyh3", Level.GOLD);
    }

    @Test
    void mockitoTest() {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);

        MailSender mockMailSender = mock(MailSender.class);

        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(0));
        assertThat(users.get(0).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(2));
        assertThat(users.get(2).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(0).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(2).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedID, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedID);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevel(User user, Level level) {
        User findUser = userDao.get(user.getId());
        assertThat(findUser.getLevel()).isEqualTo(level);
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
                throw new TxUserServiceException("강제 예외");
            }
            super.upgradeLevel(user);
        }
    }

    static class TxUserServiceException extends RuntimeException {
        public TxUserServiceException(String message) {
            super(message);
        }
    }

    @Test
    void upgradeAllOrNothing() {
        for (User user : users) {
            userDao.add(user);
        }

        TxTestUserService testService = new TxTestUserService(userDao, users.get(2).getId());
        testService.setMailSender(mailSender);

        UserServiceTx service = new UserServiceTx(testService, transactionManager);

        assertThatThrownBy(service::upgradeLevels)
                .isInstanceOf(TxUserServiceException.class);

        checkLevelUpgraded(users.get(0), false);
    }

    @Test
    void upgradeAllOrNothing_handler() {
        for (User user : users) {
            userDao.add(user);
        }

        TxTestUserService testService = new TxTestUserService(userDao, users.get(2).getId());
        testService.setMailSender(mailSender);

        UserService service = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                new TransactionHandler(testService, transactionManager, "upgradeLevel"));

        assertThatThrownBy(service::upgradeLevels)
                .isInstanceOf(TxUserServiceException.class);

        checkLevelUpgraded(users.get(0), false);
    }

}