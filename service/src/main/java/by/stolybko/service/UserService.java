package by.stolybko.service;

import by.stolybko.database.User;
import by.stolybko.database.UserDao;

import java.util.List;

public class UserService {
    private final UserDao userDao = new UserDao();

    public List<User> getAll() {
        return userDao.findAll();
    }
}
