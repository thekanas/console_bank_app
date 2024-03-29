package by.stolybko.service.bank;

import by.stolybko.database.dto.TransactionCreateDTO;
import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.User;
import by.stolybko.service.service.AccountService;
import by.stolybko.service.service.TransactionService;
import by.stolybko.service.service.UserService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import static by.stolybko.service.validator.MenuInputValidator.validationMenuInput;
import static by.stolybko.service.validator.MenuInputValidator.validationMoneyInput;

/**
 * класс предоставляет методы для совершения основных операций консольного банковского приложения
 */
@RequiredArgsConstructor
public class BankManagement {
    private Scanner scanner = new Scanner(System.in);
    private final Bank bank;
    private final UserService userService = UserService.getInstance();
    private final AccountService accountService = AccountService.getInstance();
    private final TransactionService transactionService = TransactionService.getInstance();

    /**
     * метод проверяет наличие клиента с указанными номером паспорта и паролем в базе данных
     *
     * @param passportNumber номер паспорта клиента
     * @param password пароль клиента
     * @return объект клиента банка
     */
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

    /**
     * метод предоставляет меню для совершения денежного перевода
     *
     * @param user объект клиента банка
     */
    public void transferMoney(User user) {
        System.out.println("\nTransfer money");
        Account accountFrom = selectAccount(user);
        System.out.println("How much money do you want to transfer?");
        BigDecimal moneyTransfer = validationMoneyInput();
        System.out.println("\nEnter the recipient's account");
        System.out.print("\n    Enter Input:");
        String accountToNumber = scanner.next();

        if(accountService.findByAccountNumber(accountToNumber).isEmpty()) {
            System.out.println("\nRecipient's account not found");
            return;
        }
        if(!accountService.hasWithdraw(accountFrom, moneyTransfer)) {
            System.out.println("\nNot enough money");
            return;
        }
        System.out.println("\n1 - Transfer " + moneyTransfer + " or ");
        System.out.println("5 - Cancellation of the operation?");

        int command = validationMenuInput();

        switch (command) {
            case 1 -> {
                if (transactionService.transfer(TransactionCreateDTO.builder()
                        .fromAccount(accountFrom)
                        .toAccount(accountService.findByAccountNumber(accountToNumber).get())
                        .amount(moneyTransfer)
                        .build())) {
                    System.out.println("\nOperation was completed successfully");
                } else {
                    System.out.println("\nOperation failed");
                }
            }
            case 5 -> {
            }
            default -> System.out.println("Incorrect operation number\n");
        }
    }

    /**
     * метод предоставляет меню для совершения снятия наличных
     *
     * @param user объект клиента банка
     */
    public void withdrawalMoney(User user) {
        System.out.println("\nWithdrawal money");
        Account account = selectAccount(user);
        System.out.println("How much money do you want to withdraw?");
        BigDecimal moneyWithdraw = validationMoneyInput();
        System.out.println("\n1 - Withdraw " + moneyWithdraw + " or ");
        System.out.println("5 - Cancellation of the operation?");

        int command = validationMenuInput();

        switch (command) {
            case 1 -> {
                if (transactionService.withdraw(TransactionCreateDTO.builder()
                                .fromAccount(account)
                                .toAccount(account)
                                .amount(moneyWithdraw)
                                .build() )) {
                    System.out.println("\nOperation was completed successfully");
                } else {
                    System.out.println("\nNot enough money");
                }
            }
            case 5 -> {
            }
            default -> System.out.println("Incorrect operation number\n");
        }
    }

    /**
     * метод предоставляет меню для совершения пополнения счета
     *
     * @param user объект клиента банка
     */
    public void accountReplenishment(User user) {
        System.out.println("\nAccountReplenishment");
        Account account = selectAccount(user);
        System.out.println("How much money do you want to insert?");
        BigDecimal moneyInsert = validationMoneyInput();
        System.out.println("1 - Insert " + moneyInsert + " or ");
        System.out.println("5 - Cancellation of the operation?");

        int command = validationMenuInput();

        switch (command) {
            case 1 -> {
                if (transactionService.insert(TransactionCreateDTO.builder()
                        .fromAccount(account)
                        .toAccount(account)
                        .amount(moneyInsert)
                        .build() )) {
                    System.out.println("\nOperation was completed successfully");
                } else {
                    System.out.println("\nOperation failed");
                }
            }
            case 5 -> {
            }
            default -> System.out.println("Incorrect operation number\n");
        }
    }

    /**
     * метод предоставляет меню для вывода информации об аккаунте в консоль
     *
     * @param user объект клиента банка
     */
    public void viewAccountInformation(User user) {
        System.out.println("\nYour accounts:");
        for (Account account : accountService.getAccountByUserAndBank(user, bank)) {
            System.out.println("Account number:" + account.getAccountNumber());
            System.out.println("Account balance:" + account.getBalance());
            System.out.println();
        }
    }

    private Account selectAccount(User user) {
        System.out.println("\nSelect an account:");
        List<Account> accounts = accountService.getAccountByUserAndBank(user, bank);
        scanner = new Scanner(System.in);
        for (int i = 1; i <= accounts.size(); i++) {
            System.out.println(i + " - " + accounts.get(i - 1).getAccountNumber());
        }

        boolean valid;
        int command;
        do {
            valid = true;
            command = validationMenuInput();
            if(command > accounts.size()){
                valid = false;
                System.out.println("Incorrect operation number\n");
            }
        } while (!valid);

        Account account = accounts.get(command - 1);
        System.out.println("\nSelected account: " + account.getAccountNumber());
        System.out.println("Balance: " + account.getBalance());
        System.out.println();


        return account;
    }
}

