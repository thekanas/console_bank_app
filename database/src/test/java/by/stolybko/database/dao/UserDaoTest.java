package by.stolybko.database.dao;

import by.stolybko.database.entity.User;
import by.stolybko.database.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class UserDaoTest extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void findAll() {
        User user1 = userDao.save(getUser("30JJ125")).get();
        User user2 = userDao.save(getUser("30JJ126")).get();
        User user3 = userDao.save(getUser("30JJ127")).get();

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Long> userIds = actualResult.stream()
                .map(User::getId)
                .toList();
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void findById() {
        User user = userDao.save(getUser("30JJ125")).get();

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void findByPassportNumber() {
        User user = userDao.save(getUser("30JJ125")).get();

        Optional<User> actualResult = userDao.findByPassportNumber(user.getPassportNumber(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void save() {
        User user = getUser("30JJ125");
        User actualResult = userDao.save(user).get();
        assertNotNull(actualResult.getId());
    }

    @Test
    void update() {
        User user = getUser("30JJ125");
        userDao.save(user);
        user.setPassportNumber("30JJ1256");
        userDao.update(user);

        User updatedUser = userDao.findById(user.getId()).get();
        assertThat(updatedUser.getPassportNumber()).isEqualTo("30JJ1256");
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    void deleteExistingEntity() {
        User user = userDao.save(getUser("30JJ125")).get();
        boolean actualResult = userDao.delete(user.getId());
        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() {
        userDao.save(getUser("30JJ125"));
        boolean actualResult = userDao.delete(100500L);
        assertFalse(actualResult);
    }

    private User getUser(String passportNumber) {
        return User.builder()
                .fullName("Andrei")
                .passportNumber(passportNumber)
                .password("a111a")
                .build();
    }
}