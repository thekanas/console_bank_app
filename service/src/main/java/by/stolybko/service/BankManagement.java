package by.stolybko.service;

import by.stolybko.database.dao.UserDao;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.User;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import static by.stolybko.service.validator.MenuInputValidator.validationMenuInput;


public class BankManagement {

    Bank bank = Bank.builder()
            .id(1)
            .name("CleverBank")
            .build();
    private final UserService userService = UserService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    public Optional<User> loginAccount(String passportNumber, String password) {
        {
            if (Objects.equals(passportNumber, "") || Objects.equals(password, "")) {
                System.out.println("All field required");
                return Optional.empty();
            }

            if (userService.findByPassportNumber(passportNumber, password).isEmpty()) {
                System.out.println("Incorrect login or password");
                return Optional.empty();
            }
            return userService.findByPassportNumber(passportNumber, password);
        }
    }

    public void transferMoney(User user) {
        System.out.println("transferMoney");
    }
    public void withdrawalMoney(User user) {
        System.out.println("\nWithdrawal money");
        Account account = selectAccount(user);
        System.out.println("\nSelected account: " + account.getAccountNumber());
        System.out.println("Balance: " + account.getBalance());
        System.out.println();
        System.out.println("How much money do you want to withdraw:");
        Scanner scanner = new Scanner(System.in);
        BigDecimal moneyWithdraw = BigDecimal.valueOf(scanner.nextDouble());
        System.out.println("1 - Withdraw " + moneyWithdraw + " or ");
        System.out.println("5 - Cancellation of the operation?");

        int command = validationMenuInput();

        switch (command) {
            case 1 -> {
                if(accountService.withdraw(account, moneyWithdraw)) {
                    System.out.println("Operation was completed successfully");
                }
                else {
                    System.out.println("Operation failed");
                }
            }
            case 5 -> {
            }
            default -> System.out.println("Incorrect operation number\n");
        }
    }

    public void accountReplenishment(User user) {
        System.out.println("\nAccountReplenishment");
        Account account = selectAccount(user);
        System.out.println("\nSelected account: " + account.getAccountNumber());
        System.out.println("Balance: " + account.getBalance());
        System.out.println();
        System.out.println("How much money do you want to insert:");
        Scanner scanner = new Scanner(System.in);
        BigDecimal moneyInsert = BigDecimal.valueOf(scanner.nextDouble());
        System.out.println("1 - Insert " + moneyInsert + " or ");
        System.out.println("5 - Cancellation of the operation?");

        int command = validationMenuInput();

        switch (command) {
            case 1 -> {
                if(accountService.insert(account, moneyInsert)) {
                    System.out.println("Operation was completed successfully");
                }
                else {
                    System.out.println("Operation failed");
                }
            }
            case 5 -> {
            }
            default -> System.out.println("Incorrect operation number\n");
        }
    }
    public void viewAccountInformation(User user) {
        System.out.println("Your accounts:");
        for (Account account : accountService.getAccountByUserAndBank(user, bank)) {
            System.out.println("Account number:" + account.getAccountNumber());
            System.out.println("Account balance:" + account.getBalance());
            System.out.println();
        }
    }

    private Account selectAccount(User user) {
        System.out.println("\nSelect an account:");
        List<Account> accounts = accountService.getAccountByUserAndBank(user, bank);
        Scanner scanner = new Scanner(System.in);
        for(int i = 1; i <= accounts.size(); i++) {
            System.out.println(i + " - " + accounts.get(i-1).getAccountNumber());
        }
        Account account = null;
        try {
            System.out.print("\n    Enter Input:");
            int choice = scanner.nextInt();
            account = accounts.get(choice - 1);
        } catch (Exception e) {
            System.out.println("Err : enter valid input\n");
        }

        return account;
    }
}

