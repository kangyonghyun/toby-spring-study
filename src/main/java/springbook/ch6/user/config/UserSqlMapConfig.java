package springbook.ch6.user.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import springbook.ch6.user.dao.UserDao;

public class UserSqlMapConfig implements SqlMapConfig {
    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("/sqlmap2.xml", UserDao.class);
    }
}
