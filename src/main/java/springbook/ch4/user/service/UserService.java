package springbook.ch4.user.service;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;

public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private final UserDao userDao;
    private final DataSource dataSource;

    public UserService(UserDao userDao, DataSource dataSource) {
        this.userDao = userDao;
        this.dataSource = dataSource;
    }

    public void save(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            userDao.getAll().stream()
                    .filter(this::canUpgradeLevel)
                    .forEach(this::upgradeLevel);
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        if (level == Level.BASIC){
            return user.getLogin() >= MIN_LOGIN_FOR_SILVER;
        }
        if (level == Level.SILVER){
            return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
        }
        if (level == Level.GOLD) {
            return false;
        }
        throw new IllegalArgumentException("Unknown level : " + level);
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

}
