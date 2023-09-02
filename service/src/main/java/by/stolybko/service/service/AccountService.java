package by.stolybko.service.service;

import by.stolybko.database.dao.AccountDao;
import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountService {

    private final AccountDao accountDao = AccountDao.getInstance();
    private final TransactionDao transactionDao = TransactionDao.getInstance();

    private static final AccountService INSTANCE = new AccountService();
    public static AccountService getInstance() {
        return INSTANCE;
    }


    public List<Account> getAccountByUserAndBank(User user, Bank bank) {
        return accountDao.findByUserIdAndBankId(user.getId(), bank.getId());

    }

    public boolean withdraw(Account account, BigDecimal money) {
        if(!hasWithdraw(account,money)) {
            return false;
        }
        Account accountWithdraw = accountDao.findById(account.getId()).get();
        accountWithdraw.setBalance(accountWithdraw.getBalance().subtract(money));
        accountDao.update(accountWithdraw);

        return true;
    }

    public boolean insert(Account account, BigDecimal money) {
        if(accountDao.findById(account.getId()).isEmpty()) {
            return false;
        }
        Account accountReplenishment = accountDao.findById(account.getId()).get();

        accountReplenishment.setBalance(accountReplenishment.getBalance().add(money));
        accountDao.update(accountReplenishment);

        return true;
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber);
    }

    public boolean hasWithdraw(Account account, BigDecimal money) {
        if (accountDao.findById(account.getId()).isEmpty()) {
            return false;
        }
        Account accountWithdraw = accountDao.findById(account.getId()).get();
        return accountWithdraw.getBalance().subtract(money).compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean accrueInterest(Double percent) {
        return accountDao.accrueInterest(percent);
    }
}
