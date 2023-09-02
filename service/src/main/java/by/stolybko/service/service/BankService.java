package by.stolybko.service.service;

import by.stolybko.database.dao.BankDao;
import by.stolybko.database.dto.BankDTO;
import by.stolybko.database.entity.Bank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankService {

    private final BankDao bankDao = BankDao.getInstance();
    private static final BankService INSTANCE = new BankService();
    public static BankService getInstance() {
        return INSTANCE;
    }

    public Bank getByName(String bankName) {
        return bankDao.findByName(bankName).orElseThrow(RuntimeException::new);
    }

    public Bank getBankById(Integer id) {
        return bankDao.findById(id).get();
    }

    public List<Bank> getAll() {
        return bankDao.findAll();
    }

    public Bank save(BankDTO bank) {
        Bank bankSawed = Bank.builder()
                .name(bank.getName())
                .build();
        return bankDao.save(bankSawed).get();
    }

    public Bank update(BankDTO bankDTO, Integer id) {

        Bank bank = Bank.builder()
                .id(id)
                .name(bankDTO.getName())
                .build();

        return bankDao.update(bank).get();
    }

    public boolean delete(Integer id) {
        if(bankDao.findById(id).isEmpty()){
            return false;
        }
        return bankDao.delete(id);
    }

}
