package by.stolybko.service.service;

import by.stolybko.database.dao.BankDao;
import by.stolybko.database.entity.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {


    @Mock
    private BankDao bankDao;
    @InjectMocks
    private BankService bankService;

    @Test
    void getByName() {
    }

    @Test
    void getBankById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}