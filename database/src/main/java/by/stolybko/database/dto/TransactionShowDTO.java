package by.stolybko.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс представляющий объект для вывода информации о транзакции.
 */
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
