package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A class representing a bank in the banking system.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Bank {
    private Integer id;
    private String name;
}
