package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    private Long id;
    private String accountNumber;
    private User owner;
    private Bank bank;
    private BigDecimal balance;
}
