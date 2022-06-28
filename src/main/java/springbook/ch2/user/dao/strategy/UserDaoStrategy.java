package springbook.ch2.user.dao.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbook.ch2.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoStrategy {

    static final Logger log = LoggerFactory.getLogger(UserDaoStrategy.class);

    private JdbcContext jdbcContext;
    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(User user) throws SQLException {
        jdbcContext.addAndDeleteContext(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    public void deleteAll() throws SQLException {
        jdbcContext.addAndDeleteContext(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        });
    }

    public User get(String id) throws SQLException {
        return jdbcContext.getContext(c -> {
            PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);
            return ps;
        });
    }

    public int getCount() throws SQLException {
        return jdbcContext.countContext(new CountStatement());
    }

}
