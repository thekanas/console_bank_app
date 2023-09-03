package by.stolybko.database.entity;

import by.stolybko.database.entity.enam.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Transaction {
    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;
}
