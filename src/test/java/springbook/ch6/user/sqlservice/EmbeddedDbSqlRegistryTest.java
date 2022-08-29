package springbook.ch6.user.sqlservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);
        return embeddedDbSqlRegistry;
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    @Test
    void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY999!@#$", "Modified9999");

        assertThatThrownBy(() -> sqlRegistry.updateSql(sqlmap))
                .isInstanceOf(SqlUpdateFailureException.class);

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}