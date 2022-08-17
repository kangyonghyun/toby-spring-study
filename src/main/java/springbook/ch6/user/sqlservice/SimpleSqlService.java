package springbook.ch6.user.sqlservice;

import springbook.ch6.user.service.SqlRetrievalFailureException;

import java.util.Map;

public class SimpleSqlService implements SqlService {
    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + " 에 대한 SQL 을 찾을 수 없습니다.");
        }
        return sql;
    }
}
