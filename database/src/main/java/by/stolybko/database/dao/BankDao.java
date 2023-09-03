package by.stolybko.database.dao;

import by.stolybko.database.connection.ConnectionPool;
import by.stolybko.database.entity.Bank;
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

/**
 * Класс предоставляющий доступ к данным банков в базе данных.
 */
@NoArgsConstructor(access = PRIVATE)
public class BankDao extends Dao<Integer, Bank> {
    private static final String SELECT_ALL = "SELECT bank_id, name FROM bank";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE bank_id = ?";
    private static final String INSERT = "INSERT INTO bank (name) VALUES(?)";
    private static final String UPDATE = "UPDATE bank SET name = ? WHERE bank_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM bank WHERE bank_id =?";
    private static final String SELECT_BY_NAME = SELECT_ALL + " WHERE name =?";

    private static final BankDao INSTANCE = new BankDao();
    public static BankDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Bank> findAll() {
        List<Bank> banks = new ArrayList<>();

        try(Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()) {
                banks.add(Bank.builder()
                        .id(resultSet.getInt("bank_id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banks;
    }

    @Override
    public Optional<Bank> findById(Integer id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(Bank.builder()
                    .id(resultSet.getInt("bank_id"))
                    .name(resultSet.getString("name"))
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bank> save(Bank entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt("bank_id"));
            }

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bank> update(Bank entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getId());
            preparedStatement.executeUpdate();

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Bank> findByName(String byName) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME)) {

            preparedStatement.setString(1, byName);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(Bank.builder()
                    .id(resultSet.getInt("bank_id"))
                    .name(resultSet.getString("name"))
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
