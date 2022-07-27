package springbook.ch5.user.service;

import springbook.ch5.user.domain.User;

import java.util.List;

public interface UserService {
    void save(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    void upgradeLevels();

}
