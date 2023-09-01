package by.stolybko.database.entity;

import by.stolybko.database.entity.enam.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;
}
