package by.stolybko.database.integration;

import by.stolybko.database.connection.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;

public class IntegrationTestBase {
    private static final String CLEAN_SQL = """
            DELETE FROM transaction;
            DELETE FROM account;
            DELETE FROM users;
            DELETE FROM bank;
            """;
    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS bank
            (
                bank_id SERIAL PRIMARY KEY,
                name    VARCHAR(60) NOT NULL
            );
            CREATE TABLE IF NOT EXISTS  users
            (
                user_id         BIGSERIAL PRIMARY KEY,
                full_name       VARCHAR(60) NOT NULL,
                passport_number VARCHAR(60) UNIQUE NOT NULL,
                password        VARCHAR(60) NOT NULL
            );
            CREATE TABLE IF NOT EXISTS  account
            (
                account_id     BIGSERIAL PRIMARY KEY,
                account_number VARCHAR(60) UNIQUE NOT NULL,
                balance        DECIMAL(15, 2)     NOT NULL,
                owner_id       BIGINT REFERENCES users (user_id),
                bank_id        INT REFERENCES bank (bank_id)
            );
            CREATE TABLE IF NOT EXISTS  transaction
            (
                transaction_id   BIGSERIAL PRIMARY KEY,
                from_account_id  BIGINT REFERENCES account (account_id),
                to_account_id    BIGINT REFERENCES account (account_id),
                amount           DECIMAL(15, 2)   NOT NULL,
                transaction_type VARCHAR(10) NOT NULL,
                created_at       TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
                        
            """;

    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (var connection = ConnectionPool.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_SQL);
        }
    }

    @BeforeEach
    void cleanData() throws SQLException {
        try (var connection = ConnectionPool.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
        }
    }
}
