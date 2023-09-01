package by.stolybko.service.bank;

import by.stolybko.database.entity.Bank;

public class ClientBank {

    public static void start() {
        Bank bank = BankFactory.getBank("CleverBank");

        new Menu(new BankManagement(bank)).start();
    }
}
