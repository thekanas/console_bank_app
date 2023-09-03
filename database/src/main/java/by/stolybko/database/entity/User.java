package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Класс представляющий клиента в банковской системе.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String fullName;
    private String passportNumber;
    private String password;
}
