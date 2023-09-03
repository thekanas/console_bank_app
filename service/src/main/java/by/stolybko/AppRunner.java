package by.stolybko;

import by.stolybko.service.bank.ClientBank;

/**
 * класс для запуска консольного банковского приложения
 */
public class AppRunner {

    public static void main(String[] args) {
        ClientBank.start();
    }
}
