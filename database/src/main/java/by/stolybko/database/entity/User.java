package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A class representing a customer in the banking system.
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
