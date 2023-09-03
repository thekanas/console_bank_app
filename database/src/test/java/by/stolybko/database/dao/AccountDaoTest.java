package by.stolybko.database.dao;

import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.User;
import by.stolybko.database.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class AccountDaoTest extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();
    private final AccountDao accountDao = AccountDao.getInstance();


    @Test
    void findAll() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        User user2 = userDao.save(getUser("50II70")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Account account2 = accountDao.save(getAccount("1235", bank, user2)).get();

        List<Account> actualResult = accountDao.findAll();

        assertThat(actualResult).hasSize(2);
        List<Long> userIds = actualResult.stream()
                .map(Account::getId)
                .toList();
        assertThat(userIds).contains(account1.getId(), account2.getId());
    }

    @Test
    void findById() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();

        Optional<Account> actualResult = accountDao.findById(account1.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(account1);
    }

    @Test
    void findByUserIdAndBankId() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        Bank bank2 = bankDao.save(getBank("Alfa")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        Account account2 = accountDao.save(getAccount("1235", bank, user1)).get();
        Account account3 = accountDao.save(getAccount("1236", bank2, user1)).get();

        List<Account> actualResult = accountDao.findByUserIdAndBankId(user1.getId(), bank.getId());

        assertThat(actualResult).hasSize(2);
        List<Long> userIds = actualResult.stream()
                .map(Account::getId)
                .toList();
        assertThat(userIds).contains(account1.getId(), account2.getId());
    }

    @Test
    void save() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = getAccount("1234", bank, user1);

        Account actualResult = accountDao.save(account1).get();
        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account = getAccount("1234", bank, user1);
        accountDao.save(account);
        account.setAccountNumber("1236");
        accountDao.update(account);

        Account updatedAccount = accountDao.findById(account.getId()).get();
        assertThat(updatedAccount.getAccountNumber()).isEqualTo("1236");
        assertThat(updatedAccount).isEqualTo(account);
    }

    @Test
    void deleteExistingEntity() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        boolean actualResult = accountDao.delete(account1.getId());
        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();
        boolean actualResult = accountDao.delete(100500L);
        assertFalse(actualResult);
    }

    @Test
    void findByAccountNumber() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = accountDao.save(getAccount("1234", bank, user1)).get();

        Optional<Account> actualResult = accountDao.findByAccountNumber(account1.getAccountNumber());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(account1);
    }

    @Test
    void accrueInterest() {
        double percent = 10d;
        double balance = 1000d;
        Bank bank = bankDao.save(getBank("Clever")).get();
        User user1 = userDao.save(getUser("50II60")).get();
        Account account1 = getAccount("1234", bank, user1);
        account1.setBalance(BigDecimal.valueOf(balance));
        accountDao.save(account1);

        boolean actualResult = accountDao.accrueInterest(percent);
        BigDecimal actualBalance = accountDao.findById(account1.getId()).get().getBalance();

        assertTrue(actualResult);
        assertEquals(0, BigDecimal.valueOf(1100d).compareTo(actualBalance));

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