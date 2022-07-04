package springbook.ch2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.ch2.user.dao.jdbctemplate.JdbcTemplateUserDao;

import javax.sql.DataSource;

@Configuration
public class JavaConfig {

    @Bean
    JdbcTemplateUserDao userDao() {
        JdbcTemplateUserDao userDao = new JdbcTemplateUserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    DataSource dataSource() {
        DataSource dataSource = new SingleConnectionDataSource("jdbc:h2:tcp://localhost/~/tobytest",
                "sa", "", true);
        return dataSource;
    }

}
