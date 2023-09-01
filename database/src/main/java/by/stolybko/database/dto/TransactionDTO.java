package by.stolybko.database.dto;

import by.stolybko.database.entity.Account;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDTO {
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
}
