package by.stolybko.database.dao;

import by.stolybko.database.entity.Bank;
import by.stolybko.database.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BankDaoTest extends IntegrationTestBase {

    private final BankDao bankDao = BankDao.getInstance();

    @Test
    void findAll() {
        Bank bank1 = bankDao.save(getBank("Clever")).get();
        Bank bank2 = bankDao.save(getBank("Alfa")).get();
        Bank bank3 = bankDao.save(getBank("Tinko")).get();

        List<Bank> actualResult = bankDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> bankIds = actualResult.stream()
                .map(Bank::getId)
                .toList();
        assertThat(bankIds).contains(bank1.getId(), bank2.getId(), bank3.getId());
    }

    @Test
    void findById() {
        Bank bank = bankDao.save(getBank("Alfa")).get();

        Optional<Bank> actualResult = bankDao.findById(bank.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(bank);
    }

    @Test
    void save() {
        Bank bank = getBank("Clever");
        Bank actualResult = bankDao.save(bank).get();
        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        Bank bank = getBank("Clever");
        bankDao.save(bank);
        bank.setName("Alfa");
        bankDao.update(bank);

        Bank updatedBank = bankDao.findById(bank.getId()).get();
        assertThat(updatedBank.getName()).isEqualTo("Alfa");
        assertThat(updatedBank).isEqualTo(bank);
    }

    @Test
    void deleteExistingEntity() {
        Bank bank = bankDao.save(getBank("Clever")).get();
        boolean actualResult = bankDao.delete(bank.getId());
        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        bankDao.save(getBank("Clever"));
        boolean actualResult = bankDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void findByName() {
        Bank bank = getBank("Clever");
        bankDao.save(bank);

        Optional<Bank> actualResult = bankDao.findByName(bank.getName());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(bank);

    }

    private Bank getBank(String name) {
        return Bank.builder()
                .name(name)
                .build();
    }
}