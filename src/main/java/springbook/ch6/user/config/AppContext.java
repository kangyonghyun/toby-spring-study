package springbook.ch6.user.config;

import org.h2.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.service.DummyMailSender;
import springbook.ch6.user.service.TestUserService;
import springbook.ch6.user.service.UserService;
import springbook.ch6.user.service.UserServiceImpl;
import springbook.ch6.user.sqlservice.EmbeddedDbSqlRegistry;
import springbook.ch6.user.sqlservice.OxmSqlServiceV2;
import springbook.ch6.user.sqlservice.SqlRegistry;
import springbook.ch6.user.sqlservice.SqlService;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Import({SqlServiceContext.class, TestAppContext.class, ProductionAppContext.class})
@ComponentScan(basePackages = "springbook.ch6")
public class AppContext {
    /**
     * DB 연결과 트랜잭션
     */
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/tobytest");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
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
}
