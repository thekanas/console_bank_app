package by.stolybko.service;

import by.stolybko.database.dao.TransactionDao;
import by.stolybko.database.dao.UserDao;

public class TransactionService {

    private final TransactionDao transactionDao = TransactionDao.getInstance();

    private static final TransactionService INSTANCE = new TransactionService();
    public static TransactionService getInstance() {
        return INSTANCE;
    }


}
