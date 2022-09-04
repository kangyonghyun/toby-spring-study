package springbook.ch6.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.service.DummyMailSender;
import springbook.ch6.user.service.TestUserService;
import springbook.ch6.user.service.UserService;

@Configuration
@Profile("test")
public class TestAppContext {

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
