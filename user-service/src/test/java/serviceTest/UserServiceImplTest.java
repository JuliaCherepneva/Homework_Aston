package serviceTest;

import org.aston.dto.UserDTO;
import org.aston.model.UserModel;
import org.aston.repository.UserRepository;
import org.aston.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
        user.setId(1);
        user.setName("Vasya");
        user.setEmail("vasya@example.com");
        user.setAge(25);
    }

    @Test
    @DisplayName("Тест добавляет пользователя в базу данных")
    void testCreateUser() {
        // Мокируем сохранение пользователя в репозитории
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Создаем DTO для теста
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Vasya");
        userDTO.setEmail("vasya@example.com");
        userDTO.setAge(25);

        // Проверяем, что сервис корректно сохраняет пользователя
        UserDTO createdUserDTO = userService.create(userDTO);
        assertNotNull(createdUserDTO);
        assertEquals(user.getName(), createdUserDTO.getName());
        assertEquals(user.getEmail(), createdUserDTO.getEmail());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Тест получает пользователя по ID")
    void testReadUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDTO result = userService.getById(1);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository).findById(1);
    }

    @Test
    @DisplayName("Тест возвращает исключение, если пользователь не найден по ID")
    void testReadUserWhenNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getById(1);
        });
        assertEquals("Пользователь с указанным ID 1 не обнаружен в БД", exception.getMessage());
    }

    @Test
    @DisplayName("Тест обновляет пользователя в базе данных")
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Petya");
        userDTO.setEmail("petya@example.com");
        userDTO.setAge(30);
        userDTO.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserDTO updatedUser = userService.update(1, userDTO);

        assertNotNull(updatedUser);
        assertEquals("Petya", updatedUser.getName());
        assertEquals("petya@example.com", updatedUser.getEmail());
        assertEquals(30, updatedUser.getAge());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Тест удаляет пользователя из базы данных")
    void testDeleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.delete(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    @DisplayName("Тест возвращает всех пользователей")
    void testGetAllUsers() {
        UserModel user1 = new UserModel();
        user1.setId(2);
        user1.setName("John");
        user1.setEmail("john@example.com");
        user1.setAge(28);

        UserModel user2 = new UserModel();
        user2.setId(3);
        user2.setName("Jane");
        user2.setEmail("jane@example.com");
        user2.setAge(22);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user1, user2));

        List<UserDTO> users = userService.getAll();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("Vasya", users.get(0).getName());
        assertEquals("John", users.get(1).getName());
        assertEquals("Jane", users.get(2).getName());
    }

    @Test
    @DisplayName("Тест возвращает пустой список, если пользователей нет")
    void testGetAllUsersWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<UserDTO> users = userService.getAll();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}
