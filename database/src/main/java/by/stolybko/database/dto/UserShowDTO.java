package by.stolybko.database.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class UserShowDTO {
        private Long id;
        private String fullName;
        private String passportNumber;
}
