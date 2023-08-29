package by.stolybko.database.dao;

import by.stolybko.database.connection.ConnectionPool;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Transaction;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TransactionDao extends Dao<Long, Transaction> {
    private static final String SELECT_ALL = "SELECT transaction_id, from_account_id, to_account_id, amount, created_at FROM transaction";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE transaction_id = ?";
    private static final String INSERT = "INSERT INTO transaction (from_account_id, to_account_id, amount, created_at) VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE transaction SET from_account_id = ?, to_account_id = ?, amount = ?, created_at = ? WHERE transaction_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM transaction WHERE transaction_id =?";

    private static final TransactionDao INSTANCE = new TransactionDao();
    public static TransactionDao getInstance() {
        return INSTANCE;
    }

    private final AccountDao accountDao = AccountDao.getInstance();

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();

        try(Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()) {
                transactions.add(Transaction.builder()
                        .id(resultSet.getLong("transaction_id"))
                        .fromAccount(accountDao.findById(resultSet.getLong("from_account_id")).get())
                        .toAccount(accountDao.findById(resultSet.getLong("to_account_id")).get())
                        .amount(resultSet.getBigDecimal("amount"))
                        .timestamp(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(Transaction.builder()
                    .id(resultSet.getLong("transaction_id"))
                    .fromAccount(accountDao.findById(resultSet.getLong("from_account_id")).get())
                    .toAccount(accountDao.findById(resultSet.getLong("to_account_id")).get())
                    .amount(resultSet.getBigDecimal("amount"))
                    .timestamp(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Transaction> save(Transaction entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, entity.getFromAccount().getId());
            preparedStatement.setLong(2, entity.getToAccount().getId());
            preparedStatement.setBigDecimal(3, entity.getAmount());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getTimestamp()));
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("transaction_id"));
            }

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Transaction> update(Transaction entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setLong(1, entity.getFromAccount().getId());
            preparedStatement.setLong(2, entity.getToAccount().getId());
            preparedStatement.setBigDecimal(3, entity.getAmount());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getTimestamp()));
            preparedStatement.setLong(5, entity.getId());
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
