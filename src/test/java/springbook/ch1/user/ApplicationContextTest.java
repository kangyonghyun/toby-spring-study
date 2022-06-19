package springbook.ch1.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.ch1.user.dao.DaoFactory;
import springbook.ch1.user.dao.UserDao;

import static org.assertj.core.api.Assertions.*;

public class ApplicationContextTest {

    @Test
    void singleton() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao1 = context.getBean("userDao", UserDao.class);
        UserDao userDao2 = context.getBean("userDao", UserDao.class);
        assertThat(userDao1).isSameAs(userDao2);
        assertThat(userDao1).isEqualTo(userDao2);
    }

    @Test
    void no_singleton() {
        UserDao userDao1 = new DaoFactory().userDao();
        UserDao userDao2 = new DaoFactory().userDao();
        assertThat(userDao1).isNotSameAs(userDao2);
        assertThat(userDao1).isNotEqualTo(userDao2);
    }
}
