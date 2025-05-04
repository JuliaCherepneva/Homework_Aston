package controllerTest;

import org.aston.controller.UserController;
import org.aston.dto.UserDTO;
import org.aston.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Подготовка тестовых данных
        UserDTO user1 = new UserDTO(1, "John Doe", "john@example.com",10);
        UserDTO user2 = new UserDTO(2, "Jane Doe", "jane@example.com",10);
        List<UserDTO> users = Arrays.asList(user1, user2);

        // Мокаем вызов метода
        when(userService.getAll()).thenReturn(users);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(get("/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        // Проверяем, что метод userService.getAll() был вызван один раз
        verify(userService, times(1)).getAll();
    }

    @Test
    void testDeleteUser() throws Exception {
        int userId = 1;

        // Выполняем запрос на удаление пользователя
        mockMvc.perform(delete("/users/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверяем, что метод userService.delete() был вызван с правильным аргументом
        verify(userService, times(1)).delete(userId);
    }
}
