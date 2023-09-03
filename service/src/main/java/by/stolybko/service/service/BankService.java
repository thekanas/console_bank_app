package by.stolybko.service.service;

import by.stolybko.database.dao.BankDao;
import by.stolybko.database.dto.BankDTO;
import by.stolybko.database.entity.Bank;

import java.util.List;

/**
 * сервисный класс банков банковской системы
 */
public class BankService {

    private final BankDao bankDao = BankDao.getInstance();
    private static final BankService INSTANCE = new BankService();
    public static BankService getInstance() {
        return INSTANCE;
    }

    /**
     * метод возвращает объект банка по его имени
     */
    public Bank getByName(String bankName) {
        return bankDao.findByName(bankName).orElseThrow(RuntimeException::new);
    }

    /**
     * метод возвращает объект банка по его идентификатору
     */
    public Bank getBankById(Integer id) {
        return bankDao.findById(id).get();
    }

    /**
     * метод возвращает список объектов банков
     */
    public List<Bank> getAll() {
        return bankDao.findAll();
    }

    /**
     * метод создает банк
     */
    public Bank save(BankDTO bank) {
        Bank bankSawed = Bank.builder()
                .name(bank.getName())
                .build();
        return bankDao.save(bankSawed).get();
    }

    /**
     * метод обновляет банк
     */
    public Bank update(BankDTO bankDTO, Integer id) {

        Bank bank = Bank.builder()
                .id(id)
                .name(bankDTO.getName())
                .build();

        return bankDao.update(bank).get();
    }

    /**
     * метод удаляет банк
     */
    public boolean delete(Integer id) {
        if(bankDao.findById(id).isEmpty()){
            return false;
        }
        return bankDao.delete(id);
    }

}
