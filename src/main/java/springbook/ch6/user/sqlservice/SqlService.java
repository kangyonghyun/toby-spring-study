package springbook.ch6.user.sqlservice;

import springbook.ch6.user.service.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
