package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;
import springbook.ch5.user.service.UserService;

import java.util.List;

import static springbook.ch5.user.service.UserServiceImpl.MIN_LOGIN_FOR_SILVER;
import static springbook.ch5.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/transactionTest.xml")
@Transactional
public class RollbackTest {

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

    @AfterEach
    @DisplayName("커밋시, 삭제")
    void after() {
        userService.deleteAll();
    }

    @Test
    @DisplayName("클래스 테스트에 @Transactional 지정할 경우, 롤백")
    void transactionalTest() {
        userService.deleteAll();
        userService.save(users.get(0));
        userService.save(users.get(1));
    }

    @Test
    @DisplayName("readOnly 일 경우, write 시 에러 -> db 마다 다르다")
    @Transactional(readOnly = true)
    void readOnlyTest() {
        userService.deleteAll();
    }

    @Test
    @DisplayName("커밋을 하고 싶을 때, 메소드 레벨에서 @Rollback(false) 지정")
    @Rollback(value = false)
    void rollbackTest() {
        userService.save(users.get(0));
        userService.save(users.get(1));
    }
}
