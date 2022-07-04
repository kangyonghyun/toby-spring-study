package springbook.ch3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.ch3.user.dao.UserDao;
import springbook.ch3.user.dao.UserDaoImpl;

import javax.sql.DataSource;

@Configuration
public class JavaConfig {

    @Bean
    UserDao userDao() {
        UserDao userDao = new UserDaoImpl(dataSource());
        return userDao;
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/tobytest",
                "sa", "", true);
        return dataSource;
    }
}
