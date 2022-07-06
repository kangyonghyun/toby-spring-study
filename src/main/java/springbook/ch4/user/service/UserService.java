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
        List<User> users = userDao.getAll();
        for (User user : users) {
            boolean changed = false;
            if (user.getLevel() != null) {
                if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                    user.setLevel(Level.SILVER);
                    changed = true;
                } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                    user.setLevel(Level.GOLD);
                    changed = true;
                }
            }
            if (changed) {
                userDao.update(user);
            }
        }
    }

}
