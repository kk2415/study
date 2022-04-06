package tobyspring.user.service;

import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

/* 각종 이벤트나 새로운 서비스 홍보기간에는 레벨정책이 변동될 수 있으니 따로 클래스로 추출했다. */
public class UserLevelUpgrader implements UserLevelUpgradePolicy {
    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /* if/else문 구조를 더 보기쉽게 바꿨다 */
    @Override
    public boolean canUpgradeLevel(User user) {
        Level curLevel = user.getLevel();
        switch(curLevel) {
            case BASIC: return user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER;
            case SILVER: return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + curLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
