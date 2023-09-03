package by.stolybko.database.dto;

import by.stolybko.database.entity.Account;
import by.stolybko.database.entity.Bank;
import by.stolybko.database.entity.Transaction;
import by.stolybko.database.entity.User;
import by.stolybko.database.entity.enam.TransactionType;
import by.stolybko.database.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChequeDTOTest extends IntegrationTestBase {


    @Test
    void testToString() {
        Bank bank = getBank("CleverBank");
        User user1 = getUser("50II60");
        Account account1 = getAccount("12345F789A", bank, user1);
        Transaction transaction1 = getTransaction(account1);

        String check = """
                +--------------------------------------+
                |              Bank cheque             |
                | Cheque:               232471339000In |
                | 04-09-2023                  00:22:19 |
                | Transaction type:             Insert |
                | Sender's bank:            CleverBank |
                | Recipient's bank:         CleverBank |
                | Sender's account:         12345F789A |
                | Recipient's account:      12345F789A |
                | Amount:                    50.00 BYN |
                +--------------------------------------+
                """;

        ChequeDTO chequeDTO = new ChequeDTO(transaction1);

        assertEquals(check, chequeDTO.toString());

    }

    private Transaction getTransaction(Account account) {
        return Transaction.builder()
                .id(1L)
                .fromAccount(account)
                .toAccount(account)
                .amount(new BigDecimal(50))
                .transactionType(TransactionType.INSERT)
                .timestamp(LocalDateTime.of(2023, 9, 4, 0, 22, 19))
                .build();
    }

    private Account getAccount(String accountNumber, Bank bank, User owner) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(new BigDecimal(999))
                .bank(bank)
                .owner(owner)
                .build();
    }
    private User getUser(String passportNumber) {
        return User.builder()
                .fullName("Andrei")
                .passportNumber(passportNumber)
                .password("a111a")
                .build();
    }
    private Bank getBank(String name) {
        return Bank.builder()
                .name(name)
                .build();
    }
}