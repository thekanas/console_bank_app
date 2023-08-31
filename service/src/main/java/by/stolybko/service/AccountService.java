package by.stolybko.service;

import by.stolybko.database.dao.AccountDao;
import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.Transaction;
import by.stolybko.database.entity.User;

import java.math.BigDecimal;
import java.util.List;

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
        if(accountDao.findById(account.getId()).isEmpty()) {
            return false;
        }
        Account accountWithdraw = accountDao.findById(account.getId()).get();
        if(accountWithdraw.getBalance().subtract(money).compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        accountWithdraw.setBalance(accountWithdraw.getBalance().subtract(money));
        accountDao.update(accountWithdraw);
        Transaction transactionWithdraw = Transaction.builder()

                .build()
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
}
