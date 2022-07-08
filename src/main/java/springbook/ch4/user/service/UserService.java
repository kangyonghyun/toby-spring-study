package springbook.ch4.user.service;

import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(this::canUpgradeLevel)
                .forEach(this::upgradeLevel);
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
