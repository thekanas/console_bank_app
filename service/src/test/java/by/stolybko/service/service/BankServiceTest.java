package by.stolybko.service.service;

import by.stolybko.database.dao.BankDao;
import by.stolybko.database.dto.BankDTO;
import by.stolybko.database.entity.Bank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        BankDTO bankDTO = getBankDTO();
        Bank bank = getBank();
        Bank bankSawed = getBankSawed();

        doReturn(Optional.of(bank)).when(bankDao).save(bankSawed);

        //создает запись в БД, а значит mock не работает
        Bank actualResult = bankService.save(bankDTO);

        assertThat(actualResult).isEqualTo(bank);
        verify(bankDao).save(bankSawed);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private Bank getBank() {
        return Bank.builder()
                .id(1)
                .name("TestBank")
                .build();
    }

    private BankDTO getBankDTO() {
        return new BankDTO("TestBank");
    }

    private Bank getBankSawed() {
        return Bank.builder()
                .name("TestBank")
                .build();
    }
}