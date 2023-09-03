package by.stolybko.service.service;

import by.stolybko.database.dao.AccountDao;
import by.stolybko.database.dao.BankDao;
import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.dao.UserDao;
import by.stolybko.database.dto.AccountDTO;
import by.stolybko.database.dto.AccountShowDTO;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * сервисный класс счетов банковской системы
 */
public class AccountService {

    private final AccountDao accountDao = AccountDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final BankDao bankDao = BankDao.getInstance();
    private final TransactionDao transactionDao = TransactionDao.getInstance();

    private static final AccountService INSTANCE = new AccountService();
    public static AccountService getInstance() {
        return INSTANCE;
    }

    /**
     * метод возвращает список аккаунтов пользователя в указанном банке
     */
    public List<Account> getAccountByUserAndBank(User user, Bank bank) {
        return accountDao.findByUserIdAndBankId(user.getId(), bank.getId());

    }

    /**
     * метод обновляет счёт клиента при снятии наличных
     */
    public boolean withdraw(Account account, BigDecimal money) {
        if(!hasWithdraw(account,money)) {
            return false;
        }
        Account accountWithdraw = accountDao.findById(account.getId()).get();
        accountWithdraw.setBalance(accountWithdraw.getBalance().subtract(money));
        accountDao.update(accountWithdraw);

        return true;
    }

    /**
     * метод обновляет счёт клиента при пополнении средств
     */
    public boolean insert(Account account, BigDecimal money) {
        if(accountDao.findById(account.getId()).isEmpty()) {
            return false;
        }
        Account accountReplenishment = accountDao.findById(account.getId()).get();

        accountReplenishment.setBalance(accountReplenishment.getBalance().add(money));
        accountDao.update(accountReplenishment);

        return true;
    }

    /**
     * метод возвращает обект счёта по номеру счета
     */
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber);
    }

    /**
     * метод проверяет возможно ли списать средства со счёта
     */
    public boolean hasWithdraw(Account account, BigDecimal money) {
        if (accountDao.findById(account.getId()).isEmpty()) {
            return false;
        }
        Account accountWithdraw = accountDao.findById(account.getId()).get();
        return accountWithdraw.getBalance().subtract(money).compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * метод начисляет указанный процент на все счета
     */
    public boolean accrueInterest(Double percent) {
        return accountDao.accrueInterest(percent);
    }

    /**
     * метод возвращает представление счёта по его индетификатору
     */
    public AccountShowDTO getAccountById(Long id) {
        return mapAccountShowDTO(accountDao.findById(id).get());
    }

    /**
     * метод возвращает все представления счётов
     */
    public List<AccountShowDTO> getAll() {
        List<AccountShowDTO> accountShowDTOList = new ArrayList<>();
        for (Account account : accountDao.findAll()) {
            accountShowDTOList.add(mapAccountShowDTO(account));
        }
        return accountShowDTOList;
    }

    /**
     * метод для создания счёта
     */
    public AccountShowDTO save(AccountDTO accountDTO) throws SQLException {
        Account accountSawed = accountDao.save(mapAccount(accountDTO)).get();
        return mapAccountShowDTO(accountSawed);
    }

    /**
     * метод для обновления счёта
     */
    public AccountShowDTO update(AccountDTO accountDTO, Long id) throws SQLException {

        Account account = mapAccount(accountDTO);
        account.setId(id);

        Account accountSawed = accountDao.update(account).get();

        return mapAccountShowDTO(accountSawed);
    }

    /**
     * метод для удаления счёта
     */
    public boolean delete(Long id) {
        if(accountDao.findById(id).isEmpty()){
            return false;
        }
        return accountDao.delete(id);
    }

    private Account mapAccount(AccountDTO accountDTO) throws SQLException {
        return Account.builder()
                .accountNumber(accountDTO.getAccountNumber())
                .owner(userDao.findById(accountDTO.getUserId()).orElseThrow(SQLException::new))
                .bank(bankDao.findById(accountDTO.getBankId()).orElseThrow(SQLException::new))
                .balance(accountDTO.getBalance())
                .build();
    }

    private AccountShowDTO mapAccountShowDTO(Account account) {
        return AccountShowDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(account.getOwner().getId())
                .bankId(account.getBank().getId())
                .balance(account.getBalance())
                .build();
    }
}
