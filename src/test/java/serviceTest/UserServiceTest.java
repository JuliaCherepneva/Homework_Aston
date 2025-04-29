package serviceTest;

import org.aston.model.UserModel;
import org.aston.service.UserServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestObjectFactory.createDefaultUser;
import static util.TestObjectFactory.createRandomUsers;
import static util.TestObjectFactory.createUserWithCustomName;
import static util.TestObjectFactory.createUserWithId;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
// Переписал тесты с учётом @MockitoSettings и других аннотаций под JUnit 5
// добавил LENIENT для пропуска обязательных стаков у моков,так как не во всех тестах мы обращаемся к
// экземпляру UserService, иногда нам нужно проверить какие-то абстрактные вещи
// + убрал сплошные импорты, вроде на прошлом вебинаре говорили,что это плохой тон
// + добавил 4 тестовых метода. Нам наверное нужно будет их привести единообразию по названиям,а можем и так оставить
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @Captor
    private ArgumentCaptor<UserModel> userCaptor;
    @InjectMocks
    private UserServiceImpl userService;
    @BeforeEach
    void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    @DisplayName("Тест добавляет экземпляр UserModel в БД")
    void testCreateUser() {
        UserModel user = createDefaultUser();
        userService.create(user);
        verify(session).save(user);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    @DisplayName("Тест возвращает экземпляр UserModel из БД по переданному userId")
    void testReadUser() {
        int userId = 1;
        UserModel expectedUser = createUserWithCustomName("Vasya");
        when(session.get(UserModel.class, userId)).thenReturn(expectedUser);
        UserModel actualUser = userService.read(userId);
        assertNotNull(actualUser);
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        verify(session).get(UserModel.class, userId);
    }

    @Test
    @DisplayName("Тест обновляет экземпляр UserModel в БД")
    void testUpdateUser() {
        UserModel user = createUserWithCustomName("Petya");
        user.setId(1);
        userService.update(user);
        verify(session).update(user);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    @DisplayName("Тест удаляет экземпляр UserModel из БД по переданному userId")
    void testDeleteUser() {
        int userId = 1;
        UserModel user = createUserWithId(userId);
        when(session.get(UserModel.class, userId)).thenReturn(user);
        userService.delete(userId);
        verify(session).delete(userCaptor.capture());
        assertEquals(userId, userCaptor.getValue().getId());
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    @DisplayName("Тест возвращает все экземпляры UserModel из БД")
    void testGetAllUsers() {
        List<UserModel> expectedUsers = createRandomUsers(5);
        Query<UserModel> mockQuery = mock(Query.class);
        when(session.createQuery("FROM UserModel", UserModel.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(expectedUsers);
        List<UserModel> actualUsers = userService.getAll();
        assertNotNull(actualUsers);
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getName(), actualUsers.get(0).getName());
        verify(session).createQuery("FROM UserModel", UserModel.class);
    }

    @Test
    @DisplayName("Тест возвращает исключение RuntimeException при ошибке в сохранении экземпляра UserModel в БД")
    void testCreateUserWhenExceptionThrownShouldNotCrash() {
        UserModel user = createDefaultUser();
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException("Ошибка в БД")).when(session).save(user);
        assertDoesNotThrow(() -> userService.create(user));
        verify(session).save(user);
        verify(transaction, never()).commit();
    }

    @Test
    @DisplayName("Тест возвращает исключение при попытке удалить экземпляр UserModel из БД при несуществующем userId")
    void testDeleteUserWhenUserNotFoundShouldNotCrash() {
        int userId = 1;
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.get(UserModel.class, userId)).thenReturn(null);
        assertDoesNotThrow(() -> userService.delete(userId));
        verify(session, never()).delete(any(UserModel.class));
        verify(transaction).commit();
    }

    @Test
    @DisplayName("Тест возвращает пустой список при отсутствии экземпляров UserModel")
    void testGetAllUsersWhenNoUsersShouldReturnEmptyList() {
        when(sessionFactory.openSession()).thenReturn(session);
        Query mockQuery = mock(Query.class);
        when(session.createQuery("FROM UserModel", UserModel.class)).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(Collections.emptyList());
        List<UserModel> users = userService.getAll();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Тест возвращает null при отсутствии экземпляров UserModel с заданным userId")
    void testReadUserWhenUserNotFoundShouldReturnNull() {
        int userId = 1;
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.get(UserModel.class, userId)).thenReturn(null);
        UserModel user = userService.read(userId);
        assertNull(user);
    }
}