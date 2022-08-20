package springbook.ch6.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {

    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = this.sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + " 를 이용해서 SQL 을 찾을 수 없습니다");
        }
        return sql;
    }
}
