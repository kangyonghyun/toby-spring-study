package springbook.ch4.user.policy;

import springbook.ch4.user.dao.UserDao;
import springbook.ch4.user.domain.Level;
import springbook.ch4.user.domain.User;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Override
    public boolean canUpgradeLevel(User user) {
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

    @Override
    public void upgradeLevels(User user) {
        user.upgradeLevel();
    }
}
