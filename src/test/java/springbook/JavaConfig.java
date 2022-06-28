package springbook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.ch2.user.dao.strategy.JdbcContext;
import springbook.ch2.user.dao.strategy.UserDaoStrategy;

import javax.sql.DataSource;

@Configuration
public class JavaConfig {

    @Bean
    public UserDaoStrategy userDao() {
        UserDaoStrategy userDao = new UserDaoStrategy();
        userDao.setJdbcContext(jdbcContext());
        return userDao;
    }
    @Bean
    public JdbcContext jdbcContext() {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSource());
        return jdbcContext;
    }
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/tobytest", "sa", "", true);
        return dataSource;
    }
}
