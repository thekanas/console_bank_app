package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * A class representing an account in the banking system.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Account {
    private Long id;
    private String accountNumber;
    private User owner;
    private Bank bank;
    private BigDecimal balance;
}
