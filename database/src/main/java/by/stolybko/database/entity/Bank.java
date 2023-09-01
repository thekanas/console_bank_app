package by.stolybko.database.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bank {
    private Integer id;
    private String name;
}
