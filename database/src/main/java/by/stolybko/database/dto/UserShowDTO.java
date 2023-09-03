package by.stolybko.database.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Класс представляющий объект для вывода информации о клиенте (без вывода пароля).
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class UserShowDTO {
        private Long id;
        private String fullName;
        private String passportNumber;
}
