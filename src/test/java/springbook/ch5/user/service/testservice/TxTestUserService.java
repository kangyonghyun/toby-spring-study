package springbook.ch5.user.service.testservice;

import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.User;
import springbook.ch5.user.service.UserServiceImpl;

public class TxTestUserService extends UserServiceImpl {
    private String id;

    public TxTestUserService(UserDao userDao, String id) {
        super(userDao);
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if (user.getId().equals(id)) {
            throw new TxUserServiceException("강제 예외");
        }
        super.upgradeLevel(user);
    }
}
