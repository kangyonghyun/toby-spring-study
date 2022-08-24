package springbook.ch6.user.sqlservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.service.SqlRetrievalFailureException;
import springbook.ch6.user.sqlservice.jaxb.SqlType;
import springbook.ch6.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlServiceV2 implements SqlService {

    private final BaseSqlService baseSqlService = new BaseSqlService();

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmap(Resource sqlmap) {
        this.oxmSqlReader.setSqlmap(sqlmap);
    }

    @PostConstruct
    private void loadSql() {
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        this.baseSqlService.setSqlReader(this.oxmSqlReader);

        this.baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return this.baseSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader {

        private Resource sqlmap = new ClassPathResource("/sqlmap2.xml", UserDao.class);
        private Unmarshaller unmarshaller;

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                StreamSource source = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);
                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmap + " 을 가져올 수 없습니다", e);
            }
        }
    }
}
