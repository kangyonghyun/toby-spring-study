package springbook.ch2.user.dao.strategy;

import springbook.ch2.user.domain.User;

import java.sql.SQLException;

public class UserDaoStrategy {

    private JdbcContext jdbcContext;
    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(User user) throws SQLException {
        jdbcContext.executeSql("insert into users(id, name, password) values (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("delete from users");
    }

    public User get(String id) throws SQLException {
        return jdbcContext.executeQuery("select * from users where id = ?", id);
    }

    public int getCount() throws SQLException {
        return jdbcContext.executeQuery2("select count(*) from users");
    }

}
