package springbook.ch5.user.service;

import org.springframework.transaction.annotation.Transactional;
import springbook.ch5.user.domain.User;

import java.util.List;

@Transactional
public interface UserService {
    void save(User user);
    @Transactional(readOnly = true)
    User get(String id);
    @Transactional(readOnly = true)
    List<User> getAll();
    void deleteAll();
    void upgradeLevels();
    void update(User user);
}
