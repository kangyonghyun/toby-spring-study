package springbook.ch1.learning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.ch1.user.dao.DaoFactory;
import springbook.ch1.user.dao.UserDao;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ApplicationContextTest {

    @Autowired
    SimpleDriverDataSource dataSource;

    @Test
    public void connection() throws SQLException {
        Connection con = dataSource.getConnection();
        assertThat(con).isNotNull();
    }

    @Test
    public void singleton() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao1 = context.getBean("userDao", UserDao.class);
        UserDao userDao2 = context.getBean("userDao", UserDao.class);
        assertThat(userDao1).isSameAs(userDao2);
        assertThat(userDao1).isEqualTo(userDao2);
    }

    @Test
    public void no_singleton() {
        UserDao userDao1 = new DaoFactory().userDao();
        UserDao userDao2 = new DaoFactory().userDao();
        assertThat(userDao1).isNotSameAs(userDao2);
        assertThat(userDao1).isNotEqualTo(userDao2);
    }

    @Test
    public void getNoBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        assertThatThrownBy(() -> context.getBean("userDa", UserDao.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    public void getDuplicateBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        assertThatThrownBy(() -> context.getBean(UserDao.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }
}
