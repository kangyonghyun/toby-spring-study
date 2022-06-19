package springbook.ch1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.DriverManagerConnectionMaker;

@Configuration
public class DaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());
        return userDao;
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DriverManagerConnectionMaker();
    }
}
