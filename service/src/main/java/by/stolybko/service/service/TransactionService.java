package by.stolybko.service.service;

import by.stolybko.database.dao.AccountDao;
import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.dto.*;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Transaction;
import by.stolybko.database.entity.enam.TransactionType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.stolybko.service.service.ChequeService.chequeSave;

public class TransactionService {

    private final TransactionDao transactionDao = TransactionDao.getInstance();
    private final AccountDao accountDao = AccountDao.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    private static final TransactionService INSTANCE = new TransactionService();
    public static TransactionService getInstance() {
        return INSTANCE;
    }

    public boolean withdraw(TransactionCreateDTO transactionCreateDTO) {
        boolean valid = accountService.withdraw(transactionCreateDTO.getFromAccount(), transactionCreateDTO.getAmount());
        if(!valid) {
            return false;
        }
        Transaction transaction = Transaction.builder()
                .fromAccount(transactionCreateDTO.getFromAccount())
                .toAccount(transactionCreateDTO.getToAccount())
                .amount(transactionCreateDTO.getAmount())
                .transactionType(TransactionType.WITHDRAWAL)
                .timestamp(LocalDateTime.now())
                .build();

        Optional<Transaction> transactionSaved = transactionDao.save(transaction);
        if(transactionSaved.isPresent()){
            chequeSave(transactionSaved.get());
            return true;
        }
        return false;

    }

    public boolean insert(TransactionCreateDTO transactionCreateDTO) {
        boolean valid = accountService.insert(transactionCreateDTO.getFromAccount(), transactionCreateDTO.getAmount());
        if(!valid) {
            return false;
        }
        Transaction transaction = Transaction.builder()
                .fromAccount(transactionCreateDTO.getFromAccount())
                .toAccount(transactionCreateDTO.getToAccount())
                .amount(transactionCreateDTO.getAmount())
                .transactionType(TransactionType.INSERT)
                .timestamp(LocalDateTime.now())
                .build();

        Optional<Transaction> transactionSaved = transactionDao.save(transaction);
        if(transactionSaved.isPresent()){
            chequeSave(transactionSaved.get());
            return true;
        }
        return false;
    }

    public boolean transfer(TransactionCreateDTO transactionCreateDTO) {

        Transaction transaction = Transaction.builder()
                .fromAccount(transactionCreateDTO.getFromAccount())
                .toAccount(transactionCreateDTO.getToAccount())
                .amount(transactionCreateDTO.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .timestamp(LocalDateTime.now())
                .build();

        if(transactionDao.transfer(transaction)){
            chequeSave(transaction);
            return true;
        }
        return false;
    }

    public TransactionShowDTO getTransactionById(Long id) {
        return mapTransactionShowDTO(transactionDao.findById(id).get());
    }

    public List<TransactionShowDTO> getAll() {
        List<TransactionShowDTO> transactionShowDTOList = new ArrayList<>();
        for (Transaction transaction : transactionDao.findAll()) {
            transactionShowDTOList.add(mapTransactionShowDTO(transaction));
        }
        return transactionShowDTOList;
    }

    public boolean save(TransactionDTO transactionDTO) throws SQLException {
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .fromAccount(accountDao.findById(transactionDTO.getFromAccountId()).orElseThrow(SQLException::new))
                .toAccount(accountDao.findById(transactionDTO.getToAccountId()).orElseThrow(SQLException::new))
                .amount(transactionDTO.getAmount())
                .build();


        switch (TransactionType.valueOf(transactionDTO.getTransactionType().toUpperCase())) {
            case INSERT -> {
                return insert(transactionCreateDTO);
            }
            case WITHDRAWAL -> {
                return  withdraw(transactionCreateDTO);
            }
            case TRANSFER -> {
                return transfer(transactionCreateDTO);
            }
            default -> throw new EnumConstantNotPresentException(TransactionType.class, transactionDTO.getTransactionType());
        }

    }

    public Transaction update(TransactionDTO transactionDTO, Long id) throws SQLException {

        Transaction transaction = mapTransaction(transactionDTO);
        transaction.setId(id);

        return transactionDao.update(transaction).get();
    }

    public boolean delete(Long id) {
        if(transactionDao.findById(id).isEmpty()){
            return false;
        }
        return transactionDao.delete(id);
    }

    private Transaction mapTransaction(TransactionDTO transactionDTO) throws SQLException {
        return Transaction.builder()
                .fromAccount(accountDao.findById(transactionDTO.getFromAccountId()).orElseThrow(SQLException::new))
                .toAccount(accountDao.findById(transactionDTO.getToAccountId()).orElseThrow(SQLException::new))
                .amount(transactionDTO.getAmount())
                .transactionType(TransactionType.valueOf(transactionDTO.getTransactionType()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    private TransactionShowDTO mapTransactionShowDTO(Transaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return TransactionShowDTO.builder()
                .id(transaction.getId())
                .fromAccountId(transaction.getFromAccount().getId())
                .toAccountId(transaction.getToAccount().getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().getName())
                .timestamp(transaction.getTimestamp().format(formatter))
                .build();
    }
}
