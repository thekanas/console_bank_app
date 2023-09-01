package by.stolybko.service.service;

import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.dto.TransactionDTO;
import by.stolybko.database.entity.Transaction;
import by.stolybko.database.entity.enam.TransactionType;

import java.time.LocalDateTime;

public class TransactionService {

    private final TransactionDao transactionDao = TransactionDao.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    private static final TransactionService INSTANCE = new TransactionService();
    public static TransactionService getInstance() {
        return INSTANCE;
    }

    public boolean withdraw(TransactionDTO transactionDTO) {
        boolean valid = accountService.withdraw(transactionDTO.getFromAccount(), transactionDTO.getAmount());
        if(!valid) {
            return false;
        }
        Transaction transaction = Transaction.builder()
                .fromAccount(transactionDTO.getFromAccount())
                .toAccount(transactionDTO.getToAccount())
                .amount(transactionDTO.getAmount())
                .transactionType(TransactionType.WITHDRAWAL)
                .timestamp(LocalDateTime.now())
                .build();

        transactionDao.save(transaction);
        return true;
    }

    public boolean insert(TransactionDTO transactionDTO) {
        boolean valid = accountService.insert(transactionDTO.getFromAccount(), transactionDTO.getAmount());
        if(!valid) {
            return false;
        }
        Transaction transaction = Transaction.builder()
                .fromAccount(transactionDTO.getFromAccount())
                .toAccount(transactionDTO.getToAccount())
                .amount(transactionDTO.getAmount())
                .transactionType(TransactionType.INSERT)
                .timestamp(LocalDateTime.now())
                .build();

        transactionDao.save(transaction);
        return true;
    }

    public boolean transfer(TransactionDTO transactionDTO) {

        Transaction transaction = Transaction.builder()
                .fromAccount(transactionDTO.getFromAccount())
                .toAccount(transactionDTO.getToAccount())
                .amount(transactionDTO.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        return transactionDao.transfer(transaction);

    }
}
