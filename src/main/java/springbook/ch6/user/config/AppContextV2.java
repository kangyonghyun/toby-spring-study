package springbook.ch6.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.service.DummyMailSender;
import springbook.ch6.user.service.TestUserService;
import springbook.ch6.user.service.UserService;
import springbook.ch6.user.service.UserServiceImpl;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.ch6")
@Import(SqlServiceContext.class)
@PropertySource("database.properties")
public class AppContextV2 {
    /**
     * DB 연결과 트랜잭션
     */
    @Autowired
    Environment env;
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        try {
            ds.setDriverClass((Class<? extends Driver>) Class.forName(env.getProperty("db.driverClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ds.setUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 애플리케이션 로직
     */
    @Autowired
    UserDao userDao;

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl(this.userDao);
        return userService;
    }

    @Profile("production")
    @Configuration
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
    public static class TestAppContext {
        @Autowired
        UserDao userDao;

        @Bean
        public UserService testUserService() {
            return new TestUserService(userDao);
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }
    }
}
