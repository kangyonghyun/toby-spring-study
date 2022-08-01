package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;
import springbook.ch5.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static springbook.ch5.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch5.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/transactionTest.xml")
public class TransactionTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

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
    }

    @Test
    @DisplayName("트랜잭션 매니저를 이용해서 트랜잭션을 미리 시작하고 다음에 시작하는 트랜잭션을 참여시킨다")
    void transactionSync() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(txDefinition);

        userService.save(users.get(0));
        userService.save(users.get(1));
        assertThat(userDao.getCount()).isEqualTo(2);

        transactionManager.rollback(status);

        assertThat(userDao.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("롤백 테스트 -> 하나의 트랜잭션으로 묶기")
    void transactionSync2() {
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(txDefinition);

        try {
            userService.deleteAll();
            userService.save(users.get(0));
            userService.save(users.get(1));
        } finally {
            transactionManager.rollback(status);
        }

        assertThat(userDao.getCount()).isEqualTo(0);
    }

}
