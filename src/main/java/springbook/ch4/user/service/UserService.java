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
                .filter(User::canUpgradeLevel)
                .forEach(this::upgradeLevel);
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

}
