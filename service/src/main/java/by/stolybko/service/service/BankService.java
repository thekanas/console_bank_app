package by.stolybko.service.service;

import by.stolybko.database.dao.BankDao;
import by.stolybko.database.entity.Bank;

public class BankService {

    private final BankDao bankDao = BankDao.getInstance();
    private static final BankService INSTANCE = new BankService();
    public static BankService getInstance() {
        return INSTANCE;
    }

    public Bank getByName(String bankName) {
        return bankDao.findByName(bankName).orElseThrow(RuntimeException::new);
    }

}
