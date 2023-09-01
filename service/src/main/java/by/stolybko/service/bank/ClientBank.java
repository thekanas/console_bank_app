package by.stolybko.service.bank;

import by.stolybko.database.entity.Bank;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientBank {
    private static final long DELAY = 30;
    public static void start() {
        Bank bank = BankFactory.getBank("CleverBank");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new CheckingInterestTask(), DELAY, DELAY, TimeUnit.SECONDS);

        new Menu(new BankManagement(bank)).start();


    }
}
