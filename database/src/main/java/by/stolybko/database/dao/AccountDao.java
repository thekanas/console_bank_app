package by.stolybko.database.dao;

import by.stolybko.database.connection.ConnectionPool;
import by.stolybko.database.entity.Account;
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
 * Класс предоставляющий доступ к данным счетов в базе данных.
 */
@NoArgsConstructor(access = PRIVATE)
public class AccountDao extends Dao<Long, Account> {

    private static final String SELECT_ALL = "SELECT account_id, account_number, balance, owner_id, bank_id FROM account";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE account_id = ?";
    private static final String INSERT = "INSERT INTO account (account_number, balance, owner_id, bank_id) VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE account SET account_number = ?, balance = ?, owner_id = ?, bank_id = ? WHERE account_id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM account WHERE account_id =?";
    private static final String SELECT_BY_USER_AND_BANK_ID = SELECT_ALL + " WHERE owner_id = ? AND bank_id = ?";
    private static final String SELECT_BY_NUMBER = SELECT_ALL + " WHERE account_number = ?";
    private static final String ACCRUE_INTEREST = "UPDATE account SET balance = balance + balance * ? /100";

    private static final AccountDao INSTANCE = new AccountDao();
    public static AccountDao getInstance() {
        return INSTANCE;
    }

    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();


    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();

        try(Connection connection = ConnectionPool.get();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()) {
                accounts.add(Account.builder()
                        .id(resultSet.getLong("account_id"))
                        .accountNumber(resultSet.getString("account_number"))
                        .balance(resultSet.getBigDecimal("balance"))
                        .owner(userDao.findById(resultSet.getLong("owner_id")).get())
                        .bank(bankDao.findById(resultSet.getInt("bank_id")).get())
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Optional<Account> findById(Long id) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(Account.builder()
                    .id(resultSet.getLong("account_id"))
                    .accountNumber(resultSet.getString("account_number"))
                    .balance(resultSet.getBigDecimal("balance"))
                    .owner(userDao.findById(resultSet.getLong("owner_id")).get())
                    .bank(bankDao.findById(resultSet.getInt("bank_id")).get())
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Account> findByUserIdAndBankId(Long userId, Integer bankId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USER_AND_BANK_ID)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                accounts.add(Account.builder()
                        .id(resultSet.getLong("account_id"))
                        .accountNumber(resultSet.getString("account_number"))
                        .balance(resultSet.getBigDecimal("balance"))
                        .owner(userDao.findById(resultSet.getLong("owner_id")).get())
                        .bank(bankDao.findById(resultSet.getInt("bank_id")).get())
                        .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Optional<Account> save(Account entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getAccountNumber());
            preparedStatement.setBigDecimal(2, entity.getBalance());
            preparedStatement.setLong(3, entity.getOwner().getId());
            preparedStatement.setInt(4, entity.getBank().getId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("account_id"));
            }

            return Optional.of(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> update(Account entity) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, entity.getAccountNumber());
            preparedStatement.setBigDecimal(2, entity.getBalance());
            preparedStatement.setLong(3, entity.getOwner().getId());
            preparedStatement.setInt(4, entity.getBank().getId());
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
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NUMBER)) {

            preparedStatement.setString(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next() ? Optional.of(Account.builder()
                    .id(resultSet.getLong("account_id"))
                    .accountNumber(resultSet.getString("account_number"))
                    .balance(resultSet.getBigDecimal("balance"))
                    .owner(userDao.findById(resultSet.getLong("owner_id")).get())
                    .bank(bankDao.findById(resultSet.getInt("bank_id")).get())
                    .build())
                    : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean accrueInterest(Double percent) {
        try (Connection connection = ConnectionPool.get();
             PreparedStatement preparedStatement = connection.prepareStatement(ACCRUE_INTEREST)) {

            preparedStatement.setDouble(1, percent);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
