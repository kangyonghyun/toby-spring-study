package springbook.ch1.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.ch1.user.connection.ConnectionMaker;
import springbook.ch1.user.connection.CountingConnectionMaker;
import springbook.ch1.user.connection.DriverManagerConnectionMaker;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DriverManagerConnectionMaker();
    }
}
