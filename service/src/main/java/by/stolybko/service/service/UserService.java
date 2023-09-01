package by.stolybko.service.service;

import by.stolybko.database.dao.UserDao;
import by.stolybko.database.entity.User;

import java.util.Optional;

public class UserService {
    private final UserDao userDao = UserDao.getInstance();

    private static final UserService INSTANCE = new UserService();
    public static UserService getInstance() {
        return INSTANCE;
    }

    public Optional<User> findByPassportNumber(String passportNumber, String password) {
        return userDao.findByPassportNumber(passportNumber, password);
    }


}
