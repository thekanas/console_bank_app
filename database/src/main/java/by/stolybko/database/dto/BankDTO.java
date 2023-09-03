package by.stolybko.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий объект на основе которого создается/выводится объект банка.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO {
    private String name;
}
