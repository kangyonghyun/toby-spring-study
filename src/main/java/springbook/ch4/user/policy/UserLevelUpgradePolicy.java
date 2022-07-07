package springbook.ch4.user.policy;

import springbook.ch4.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevels(User user);
}
