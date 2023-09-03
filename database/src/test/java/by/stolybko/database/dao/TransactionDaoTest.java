package by.stolybko.database.dao;

import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.Transaction;
import by.stolybko.database.entity.User;
import by.stolybko.database.entity.enam.TransactionType;
import by.stolybko.database.integration.IntegrationTestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;


class TransactionDaoTest extends IntegrationTestBase {
    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();
    private final AccountDao accountDao = AccountDao.getInstance();
    private final TransactionDao transactionDao = TransactionDao.getInstance();



    @Test
    void findAll() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = transactionDao.save(getTransaction(account1)).get();
        Transaction transaction2 = transactionDao.save(getTransaction(account1)).get();
        Transaction transaction3 = transactionDao.save(getTransaction(account1)).get();

        List<Transaction> actualResult = transactionDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Long> trnIds = actualResult.stream()
                .map(Transaction::getId)
                .toList();
        assertThat(trnIds).contains(transaction1.getId(), transaction2.getId(), transaction3.getId());

    }

    @Test
    void findById() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = transactionDao.save(getTransaction(account1)).get();

        Optional<Transaction> actualResult = transactionDao.findById(transaction1.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(transaction1);
    }

    @Test
    void save() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = getTransaction(account1);

        Transaction actualResult = transactionDao.save(transaction1).get();
        assertNotNull(actualResult.getId());

    }

    @Test
    void update() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = getTransaction(account1);
        transactionDao.save(transaction1);
        transaction1.setAmount(new BigDecimal(111));
        transactionDao.update(transaction1);

        Transaction updatedTransaction = transactionDao.findById(transaction1.getId()).get();
        assertEquals(0, BigDecimal.valueOf(111).compareTo(updatedTransaction.getAmount()));
        assertThat(updatedTransaction).isEqualTo(transaction1);

    }

    @Test
    void deleteExistingEntity() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = transactionDao.save(getTransaction(account1)).get();
        boolean actualResult = transactionDao.delete(transaction1.getId());
        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Transaction transaction1 = transactionDao.save(getTransaction(account1)).get();
        boolean actualResult = transactionDao.delete(100500L);
        assertFalse(actualResult);
    }

    @Test
    void transferSuccess() {
        Bank bank1 = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(Account.builder()
                        .accountNumber("1234")
                        .owner(user1)
                        .bank(bank1)
                        .balance(new BigDecimal(100))
                        .build()).get();

        Bank bank2 = bankDao.save(getBank("Alfa")).get();
        User user2 = userDao.save(getUser("50II65")).get();
        Account account2 = accountDao.save(Account.builder()
                .accountNumber("1235")
                .owner(user2)
                .bank(bank2)
                .balance(new BigDecimal(200))
                .build()).get();

        Transaction transaction1 = Transaction.builder()
                .fromAccount(account1)
                .toAccount(account2)
                .amount(new BigDecimal(50))
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .fromAccount(account1)
                .toAccount(account2)
                .amount(new BigDecimal(50))
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        boolean actualResult1 = transactionDao.transfer(transaction1);
        boolean actualResult2 = transactionDao.transfer(transaction2);

        List<Transaction> actualResult = transactionDao.findAll();

        assertThat(actualResult).hasSize(2);

        assertTrue(actualResult1);
        assertTrue(actualResult2);

        assertEquals(0, BigDecimal.valueOf(0).compareTo(accountDao.findById(account1.getId()).get().getBalance()));
        assertEquals(0, BigDecimal.valueOf(300).compareTo(accountDao.findById(account2.getId()).get().getBalance()));

    }

    @Disabled("does not work together with other tests")
    @Test
    void transferNotSuccess_NotEnoughMoney() {
        Bank bank1 = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(Account.builder()
                .accountNumber("1234")
                .owner(user1)
                .bank(bank1)
                .balance(new BigDecimal(100))
                .build()).get();

        Bank bank2 = bankDao.save(getBank("Alfa")).get();
        User user2 = userDao.save(getUser("50II65")).get();
        Account account2 = accountDao.save(Account.builder()
                .accountNumber("1235")
                .owner(user2)
                .bank(bank2)
                .balance(new BigDecimal(200))
                .build()).get();

        Transaction transaction1 = Transaction.builder()
                .fromAccount(account1)
                .toAccount(account2)
                .amount(new BigDecimal(101))
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        boolean actualResult1 = transactionDao.transfer(transaction1);

        List<Transaction> actualResult = transactionDao.findAll();

        assertThat(actualResult).hasSize(0);

        assertFalse(actualResult1);

        assertEquals(0, BigDecimal.valueOf(100).compareTo(accountDao.findById(account1.getId()).get().getBalance()));
        assertEquals(0, BigDecimal.valueOf(200).compareTo(accountDao.findById(account2.getId()).get().getBalance()));

    }

    private Transaction getTransaction(Account account) {
        return Transaction.builder()
                .fromAccount(account)
                .toAccount(account)
                .amount(new BigDecimal(999))
                .transactionType(TransactionType.INSERT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Account getAccount(String accountNumber, Bank bank, User owner) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(new BigDecimal(999))
                .bank(bank)
                .owner(owner)
                .build();
    }
    private User getUser(String passportNumber) {
        return User.builder()
                .fullName("Andrei")
                .passportNumber(passportNumber)
                .password("a111a")
                .build();
    }
    private Bank getBank(String name) {
        return Bank.builder()
                .name(name)
                .build();
    }
}