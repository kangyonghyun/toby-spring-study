package springbook.ch5.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.Level;
import springbook.ch5.user.domain.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    private final UserDao userDao;
    private MailSender mailSender;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void save(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void upgradeLevels() {
        userDao.getAll().stream()
                .filter(this::canUpgradeLevel)
                .forEach(this::upgradeLevel);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public int getCount() {
        return userDao.getCount();
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
        sendUpgradeMail(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("hmoon826@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다");
        this.mailSender.send(mailMessage);
    }
}
