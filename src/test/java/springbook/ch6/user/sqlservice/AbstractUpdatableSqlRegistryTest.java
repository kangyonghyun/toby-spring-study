package springbook.ch6.user.sqlservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public abstract class AbstractUpdatableSqlRegistryTest {
    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Test
    void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1);
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2);
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3);
    }

    @Test
    void unKnownKey() {
        assertThatThrownBy(() -> sqlRegistry.findSql("#KEY1"))
                .isInstanceOf(SqlNotFoundException.class);
    }

    @Test
    void updateSingle() {
        sqlRegistry.updateSql("KEY1", "USQL1");
        checkFindResult("USQL1", "SQL2", "SQL3");
    }

    @Test
    void unknownKey_update() {
        assertThatThrownBy(() -> sqlRegistry.updateSql("@KEY1", "USQL1"))
                .isInstanceOf(SqlUpdateFailureException.class);
    }

    @Test
    void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "KSQL1");
        sqlmap.put("KEY3", "KSQL3");
        sqlRegistry.updateSql(sqlmap);

        checkFindResult("KSQL1", "SQL2", "KSQL3");
    }
}
