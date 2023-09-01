package by.stolybko.service.bank;

import by.stolybko.database.entity.Bank;
import by.stolybko.service.service.BankService;

public class BankFactory {

    private static final BankService bankService = BankService.getInstance();


    public static Bank getBank(String bankName) {
        return bankService.getByName(bankName);
    }
}
