import org.aston.model.UserModel;
import org.aston.service.UserServiceImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для класса UserService.
 * Использует Testcontainers для запуска временной базы данных PostgreSQL.
 */
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceIntegrationTest {

    private SessionFactory sessionFactory;
    private UserServiceImpl userService;

    @Container
    private static final PostgresTestContainer postgresContainer = PostgresTestContainer.getInstance();

    /**
     * Настройка Hibernate и создание UserService перед всеми тестами.
     */
    @BeforeAll
    void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.addAnnotatedClass(UserModel.class);

        sessionFactory = configuration.buildSessionFactory();
        userService = new UserServiceImpl(sessionFactory);
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    /**
     * Очистка таблицы пользователей перед каждым тестом.
     */
    @BeforeEach
    void cleanUp() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserModel").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void testCreateAndReadUser() {
        UserModel user = new UserModel("Володя", "Володя@mail.com", 30, LocalDateTime.now());
        userService.create(user);

        List<UserModel> users = userService.getAll();

        assertEquals(1, users.size());
        UserModel savedUser = users.get(0);
        assertEquals("Володя", savedUser.getName());
        assertEquals("Володя@mail.com", savedUser.getEmail());
    }

    @Test
    void testUpdateUser() {
        UserModel user = new UserModel("Володя", "Володя@mail.com", 28, LocalDateTime.now());
        userService.create(user);

        user = userService.getAll().get(0);
        user.setName("Володя");
        userService.update(user);

        UserModel updatedUser = userService.read(user.getId());
        assertEquals("Володя", updatedUser.getName());
    }

    @Test
    void testDeleteUser() {
        UserModel user = new UserModel("Володя", "Володя@mail.com", 40, LocalDateTime.now());
        userService.create(user);

        user = userService.getAll().get(0);
        userService.delete(user.getId());

        List<UserModel> users = userService.getAll();
        assertTrue(users.isEmpty());
    }
}
