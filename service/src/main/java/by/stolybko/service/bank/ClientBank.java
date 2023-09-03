package by.stolybko.service.bank;

import by.stolybko.database.entity.Bank;
import by.stolybko.service.util.PropertiesManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * класс для запуска меню консоли заданного банка и служебных задач банка
 */
public class ClientBank {
    private static final long DELAY = 30;
    public static void start() {
        Bank bank = BankFactory.getBank(PropertiesManager.get("bankName"));

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new CheckingInterestTask(), DELAY, DELAY, TimeUnit.SECONDS);

        new Menu(new BankManagement(bank)).start();


    }
}
