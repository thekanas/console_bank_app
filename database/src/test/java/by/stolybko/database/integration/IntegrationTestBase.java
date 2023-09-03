package by.stolybko.database.integration;

import by.stolybko.database.connection.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.management.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    /*protected void showContentTable(String tableName) {

        try (var connection = ConnectionPool.get();
             Statement statement = connection.createStatement()) {
            Query query = connection.("select * from " + tableName);
            ResultSet resultSet = statement.executeQuery("select * from " + tableName);

            List<Object[]> results = resultSet.getObject();
            System.out.println("\n======================================");
            System.out.println("Content table \"" + tableName + "\":");
            System.out.println("--------------------------------------");
            int cnt = 0;
            for (Object[] obj : results) {
                cnt++;
                System.out.println("Row: " + cnt);
                for (Object objItem : obj) {
                    System.out.println("\t" + objItem);
                }
            }
            System.out.println("======================================\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
