package by.stolybko.database.dao;

import by.stolybko.database.entity.User;
import by.stolybko.database.connection.ConnectionPool;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserDao extends Dao<Long, User>{
    private static final String SELECT_ALL = "SELECT user_id, full_name, passport_number FROM users";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO users (full_name, passport_number, password) VALUES(?,?,?)";
    private static final String UPDATE = "UPDATE users SET full_name = ?, passport_number = ?, password = ? WHERE user_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE user_id =?";
    private static final String SELECT_BY_PASSPORT_NUMBER = SELECT_ALL + " WHERE passport_number = ? and password = ?";

    private static final UserDao INSTANCE = new UserDao();
    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();


        try(Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()) {
                users.add(User.builder()
                        .id(resultSet.getLong("user_id"))
                        .fullName(resultSet.getString("full_name"))
                        .passportNumber(resultSet.getString("passport_number"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(User.builder()
                    .id(resultSet.getLong("user_id"))
                    .fullName(resultSet.getString("full_name"))
                    .passportNumber(resultSet.getString("passport_number"))
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> findByPassportNumber(String passportNumber, String password) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_PASSPORT_NUMBER)) {

            preparedStatement.setString(1, passportNumber);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(User.builder()
                    .id(resultSet.getLong("user_id"))
                    .fullName(resultSet.getString("full_name"))
                    .passportNumber(resultSet.getString("passport_number"))
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> save(User entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPassportNumber());
            preparedStatement.setObject(3, entity.getPassword());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("user_id"));
            }

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> update(User entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPassportNumber());
            preparedStatement.setObject(3, entity.getPassword());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
