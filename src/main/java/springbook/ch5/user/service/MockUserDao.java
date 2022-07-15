package springbook.ch5.user.service;

import springbook.ch5.user.dao.UserDao;
import springbook.ch5.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {
    private List<User> users;
    private List<User> updatedUsers = new ArrayList<>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    @Override
    public void update(User user) {
        updatedUsers.add(user);
    }

    public List<User> getUpdatedUsers() {
        return updatedUsers;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void add(User user) {
        throw new AssertionError();
    }

    @Override
    public User get(String id) {
        throw new AssertionError();
    }

    @Override
    public void deleteAll() {
        throw new AssertionError();
    }

    @Override
    public int getCount() {
        throw new AssertionError();
    }

}
