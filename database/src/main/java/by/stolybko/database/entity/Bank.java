package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Класс представляющий банк в банковской системе.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Bank {
    private Integer id;
    private String name;
}
