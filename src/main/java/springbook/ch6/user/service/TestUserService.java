package springbook.ch6.user.service;

import springbook.ch6.user.dao.UserDao;
import springbook.ch6.user.domain.User;

import java.util.List;

public class TestUserService extends UserServiceImpl {
    private String id = "kyh3_ex";

    public TestUserService(UserDao userDao) {
        super(userDao);
    }

    @Override
    protected void upgradeLevel(User user) {
        if (user.getId().equals(id)) {
            throw new TxUserServiceException("강제 예외");
        }
        super.upgradeLevel(user);
    }
    @Override
    public List<User> getAll() {
        for (User user : super.getAll()) {
            update(user);
        }
        return null;
    }
}
