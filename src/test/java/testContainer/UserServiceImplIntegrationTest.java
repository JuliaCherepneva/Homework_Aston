package testContainer;

import org.aston.Application;
import org.aston.dto.UserDTO;
import org.aston.repository.UserRepository;
import org.aston.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    private UserDTO testUser;

    static {
        PostgresTestContainer.getInstance().start(); // запускаем контейнер
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresTestContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.getInstance()::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = UserDTO.builder()
                .name("Тестовый пользователь")
                .email("test@example.com")
                .age(25)
                .build();
    }


    @Test
    void testCreateUser() {
        UserDTO saved = userService.create(testUser);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Тестовый пользователь");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void testGetById() {
        UserDTO saved = userService.create(testUser);
        UserDTO found = userService.getById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testGetAll() {
        userService.create(testUser);
        userService.create(UserDTO.builder()
                .name("Второй пользователь")
                .email("second@example.com")
                .age(30)
                .build());

        List<UserDTO> all = userService.getAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void testDeleteUser() {
        UserDTO saved = userService.create(testUser);
        userService.delete(saved.getId());

        assertThatThrownBy(() -> userService.getById(saved.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не обнаружен");
    }

    @Test
    void testUpdateUser() {
        UserDTO saved = userService.create(testUser);
        UserDTO update = UserDTO.builder()
                .name("Обновлённый пользователь")
                .email("updated@example.com")
                .age(35)
                .build();

        UserDTO updated = userService.update(saved.getId(), update);

        assertThat(updated.getName()).isEqualTo("Обновлённый пользователь");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getAge()).isEqualTo(35);
    }
}
