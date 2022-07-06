package springbook.ch4.user.service;

import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

import java.util.List;

public class UserService {

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
        Level userLevel = user.getLevel();
        if (userLevel == Level.BASIC){
            return user.getLogin() >= 50;
        }
        if (userLevel == Level.SILVER){
            return user.getRecommend() >= 30;
        }
        if (userLevel == Level.GOLD) {
            return false;
        }
        throw new IllegalArgumentException("Unknown level : " + user.getLevel());
    }

    private void upgradeLevel(User user) {
        if (user.getLevel() == Level.BASIC) {
            user.setLevel(Level.SILVER);
        } else if (user.getLevel() == Level.SILVER) {
            user.setLevel(Level.GOLD);
        } else {
            return;
        }
        userDao.update(user);
    }

}
