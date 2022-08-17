package springbook.ch6.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.ch6.user.domain.Level;
import springbook.ch6.user.domain.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class MapUserDaoImpl implements UserDao {

    private Map<String, String> sqlMap;
    private final JdbcTemplate jdbcTemplate;

    public MapUserDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    @Override
    public void add(User user) {
        jdbcTemplate.update(this.sqlMap.get("add"),
                user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(this.sqlMap.get("get"),
                userRowMapper, id);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(this.sqlMap.get("getAll"), userRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(this.sqlMap.get("deleteAll"));
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForObject(this.sqlMap.get("getCount"), Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(this.sqlMap.get("update"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }
}
