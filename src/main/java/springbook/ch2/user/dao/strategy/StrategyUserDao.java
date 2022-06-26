package springbook.ch2.user.dao.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.ch2.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StrategyUserDao {

    static final Logger log = LoggerFactory.getLogger(StrategyUserDao.class);
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
//        class AddStatement implements StatementStrategy {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//                return ps;
//            }
//        }
//        addContext(new AddStatement(user));
//        addContext(new AddStatement());
        addAndDeleteContext(new StatementStrategy() {
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

    public User get(String id) throws SQLException {
        return getContext(new GetStatement(id));
    }

    public void deleteAll() throws SQLException {
        addAndDeleteContext(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("delete from users");
            }
        });
    }

    public int getCount() throws SQLException {
        return countContext(new CountStatement());
    }

    public void deleteContext(StatementStrategy stmt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();

            ps = stmt.makePreparedStatement(con);

            ps.executeUpdate();
        } catch (SQLException e) {
            log.info("delete Exception!!!");
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("ps.close() error", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info("con.close() error", e);
                }
            }
        }
    }

    public int countContext(StatementStrategy stmt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            ps = stmt.makePreparedStatement(con);

            rs = ps.executeQuery();

            rs.next();
            int count = rs.getInt(1);

            return count;
        } catch (SQLException e) {
            log.info("get count Exception!!!");
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.info("rs.close() error", e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("ps.close() error", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info("con.close() error", e);
                }
            }
        }
    }

    public User getContext(StatementStrategy stmt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();

            ps = stmt.makePreparedStatement(con);

            rs = ps.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (user == null) {
                throw new EmptyResultDataAccessException(1);
            }

            return user;
        } catch (SQLException e) {
            log.info("get Exception!!!");
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.info("rs.close() error", e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("ps.close() error", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info("con.close() error", e);
                }
            }
        }
    }

    public void addContext(StatementStrategy stmt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            log.info("connection = {}", con.getClass());

            ps = stmt.makePreparedStatement(con);

            ps.executeUpdate();
        } catch (SQLException e) {
            log.info("add Exception!!!");
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("ps.close() error", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info("con.close() error", e);
                }
            }
        }
    }

    public void addAndDeleteContext(StatementStrategy stmt) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            log.info("connection = {}", con.getClass());

            ps = stmt.makePreparedStatement(con);

            ps.executeUpdate();
        } catch (SQLException e) {
            log.info("add Exception!!!");
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("ps.close() error", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    log.info("con.close() error", e);
                }
            }
        }
    }

}
