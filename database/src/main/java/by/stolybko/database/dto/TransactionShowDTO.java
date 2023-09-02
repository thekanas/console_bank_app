package by.stolybko.database.dto;

import by.stolybko.database.entity.enam.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionShowDTO {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private String transactionType;
    private String timestamp;
}
