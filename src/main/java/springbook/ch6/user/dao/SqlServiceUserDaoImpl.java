package springbook.ch6.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import springbook.ch6.user.domain.Level;
import springbook.ch6.user.domain.User;
import springbook.ch6.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SqlServiceUserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private SqlService sqlService;

    @Autowired
    public SqlServiceUserDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
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
        jdbcTemplate.update(sqlService.getSql("add"),
                user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlService.getSql("get"),
                userRowMapper, id);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("getAll"), userRowMapper);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("deleteAll"));
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForObject(sqlService.getSql("getCount"), Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(sqlService.getSql("update"),
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }
}
