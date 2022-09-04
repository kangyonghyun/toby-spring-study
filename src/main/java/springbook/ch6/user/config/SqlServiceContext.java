package springbook.ch6.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import springbook.ch6.user.sqlservice.EmbeddedDbSqlRegistry;
import springbook.ch6.user.sqlservice.OxmSqlServiceV2;
import springbook.ch6.user.sqlservice.SqlRegistry;
import springbook.ch6.user.sqlservice.SqlService;

import javax.sql.DataSource;

@Configuration
public class SqlServiceContext {
    /**
     * SQL 서비스
     */
    @Bean
    public SqlService sqlService() {
        OxmSqlServiceV2 sqlService = new OxmSqlServiceV2();
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setUnmarshaller(unmarshaller());
        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("springbook.ch6.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/schema.sql")
                .build();
    }
}
