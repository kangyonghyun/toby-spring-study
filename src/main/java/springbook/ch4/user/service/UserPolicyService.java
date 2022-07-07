package springbook.ch4.user.service;

import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;
import springbook.ch4.user.policy.UserLevelUpgradePolicy;

public class UserPolicyService {

    private final UserDao userDao;
    private final UserLevelUpgradePolicy userLevelUpgradePolicy;

    public UserPolicyService(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userDao = userDao;
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void save(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(userLevelUpgradePolicy::canUpgradeLevel)
                .forEach(this::upgradeLevel);
    }

    private void upgradeLevel(User user) {
        userLevelUpgradePolicy.upgradeLevels(user);
        userDao.update(user);
    }

}
