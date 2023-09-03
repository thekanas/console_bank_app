package by.stolybko.service.bank;

import by.stolybko.database.entity.Bank;
import by.stolybko.service.service.BankService;


public class BankFactory {

    private static final BankService bankService = BankService.getInstance();

    /**
     * метод возвращает объект банк по указанному названию банка
     * @param bankName название банка (должно соответствовать названию банка в базеданных)
     * @return объект банка
     */
    public static Bank getBank(String bankName) {
        return bankService.getByName(bankName);
    }
}
