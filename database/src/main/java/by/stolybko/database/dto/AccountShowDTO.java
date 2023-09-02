package by.stolybko.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountShowDTO {
    private Long id;
    private String accountNumber;
    private Long userId;
    private Integer bankId;
    private BigDecimal balance;
}
