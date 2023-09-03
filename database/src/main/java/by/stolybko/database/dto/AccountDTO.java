package by.stolybko.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс представляющий объект на основе которого создается объект счёта.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private Long userId;
    private Integer bankId;
    private BigDecimal balance;
}
