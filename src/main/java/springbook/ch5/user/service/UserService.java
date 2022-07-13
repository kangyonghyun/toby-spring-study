package springbook.ch5.user.service;

import springbook.ch5.user.domain.User;

public interface UserService {
    void save(User user);

    void upgradeLevels();
}
