package by.stolybko.service;

import by.stolybko.database.entity.User;

import java.util.Scanner;

import static by.stolybko.service.validator.MenuInputValidator.validationMenuInput;

public class Menu {
    private final BankManagement bankManagement = new BankManagement();

    public void start() {
        String passportNumber = "";
        String password = "";

        while (true) {
            System.out.println("\n ->|| Welcome to CleverBank    ||<- \n");
            System.out.println("1 - Login Account");
            System.out.println("2 - Close app");


            int command = validationMenuInput();

            switch (command) {
                case 1 -> {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter  passport number:");
                    passportNumber = scanner.next();
                    System.out.print("Enter  password:");
                    password = scanner.next();
                    if (bankManagement.loginAccount(passportNumber, password).isPresent()) {
                        User user = bankManagement.loginAccount(passportNumber, password).get();
                        startAccountMenu(user);

                        System.out.println("Logout Successfully!\n");
                    } else {
                        System.out.println("ERR : login Failed!\n");
                    }
                }
                case 2 -> {
                    System.out.println("Exited Successfully!\n\nThank You :)");
                    System.exit(0);
                }
                default -> System.out.println("Incorrect operation number\n");
            }

        }
    }

    private void startAccountMenu(User user) {

        System.out.println("Hello, " + user.getFullName());
        boolean exit = false;
        while (!exit) {

            System.out.println("\n1 - Transfer money");
            System.out.println("2 - Withdrawal money");
            System.out.println("3 - Account replenishment");
            System.out.println("4 - View account information");
            System.out.println("5 - LogOut");

            int command = validationMenuInput();

            switch (command) {
                case 1 -> {
                    bankManagement.transferMoney(user);
                }
                case 2 -> {
                    bankManagement.withdrawalMoney(user);
                }
                case 3 -> {
                    bankManagement.accountReplenishment(user);
                }
                case 4 -> {
                    bankManagement.viewAccountInformation(user);
                }
                case 5 -> {
                    exit = true;
                }
                default -> System.out.println("Incorrect operation number\n");

            }
        }
    }
}