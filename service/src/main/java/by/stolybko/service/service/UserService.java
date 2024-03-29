package by.stolybko.service.service;

import by.stolybko.database.dao.UserDao;
import by.stolybko.database.dto.UserDTO;
import by.stolybko.database.dto.UserShowDTO;
import by.stolybko.database.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * сервисный класс клиентов банковской системы
 */
public class UserService {
    private final UserDao userDao = UserDao.getInstance();

    private static final UserService INSTANCE = new UserService();
    public static UserService getInstance() {
        return INSTANCE;
    }

    /**
     * метод возвращает представление пользователя по его идентификатору
     */
    public UserShowDTO getUserById(Long id) throws SQLException {

        Optional<User> user = userDao.findById(id);
        if(user.isEmpty()) {
            throw new SQLException();
        }
        return mapUserShowDTO(user.get());
    }

    /**
     * метод возвращает представления всех пользователей
     */
    public List<UserShowDTO> getAll() {
        List<UserShowDTO> users = new ArrayList<>();
        for(User user : userDao.findAll()) {
            users.add(mapUserShowDTO(user));
        }
        return users;
    }

    /**
     * метод создаёт пользователя
     */
    public UserShowDTO save(UserDTO user) {
        User userSawed = userDao.save(mapUser(user)).get();
        return mapUserShowDTO(userSawed);
    }

    /**
     * метод обновляет пользователя
     */
    public UserShowDTO update(UserDTO userDTO, Long id) {

        User user = User.builder()
                .id(id)
                .fullName(userDTO.getFullName())
                .passportNumber(userDTO.getPassportNumber())
                .password(userDTO.getPassword())
                .build();

        User userSawed = userDao.update(user).get();
        return mapUserShowDTO(userSawed);
    }

    /**
     * метод удаляет пользователя
     */
    public boolean delete(Long id) {
        if(userDao.findById(id).isEmpty()){
            return false;
        }
        return userDao.delete(id);
    }

    /**
     * метод возвращает объект пользователя по его номеру паспорта и паролю
     */
    public Optional<User> findByPassportNumber(String passportNumber, String password) {
        return userDao.findByPassportNumber(passportNumber, password);
    }

    private UserShowDTO mapUserShowDTO(User user) {
        return UserShowDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .build();
    }

    private User mapUser (UserDTO user) {
        return User.builder()
                .fullName(user.getFullName())
                .passportNumber(user.getPassportNumber())
                .password(user.getPassword())
                .build();
    }


}
