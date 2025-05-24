package controllerTest;

import org.aston.assembler.UserModelAssembler;
import org.aston.controller.UserController;
import org.aston.dto.UserDTO;
import org.aston.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserModelAssembler assembler;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user1 = new UserDTO(1, "John Doe", "john@example.com", 10, LocalDateTime.now());
        UserDTO user2 = new UserDTO(2, "Jane Doe", "jane@example.com", 10, LocalDateTime.now());

        List<UserDTO> users = Arrays.asList(user1, user2);

        when(userService.getAll()).thenReturn(users);
        when(assembler.toModel(user1)).thenReturn(user1);
        when(assembler.toModel(user2)).thenReturn(user2);

        mockMvc.perform(get("/users/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Jane Doe"));

        verify(userService, times(1)).getAll();
        verify(assembler, times(1)).toModel(user1);
        verify(assembler, times(1)).toModel(user2);
    }

    @Test
    void testDeleteUser() throws Exception {
        int userId = 1;

        mockMvc.perform(delete("/users/delete/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO createdUser = new UserDTO(1, "John Smith", "johnsmith@example.com", 10, LocalDateTime.now());

        when(userService.create(any(UserDTO.class))).thenReturn(createdUser);
        when(assembler.toModel(createdUser)).thenReturn(createdUser); // <--- ЭТО ВАЖНО

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"johnsmith@example.com\",\"groupId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("johnsmith@example.com"));

        verify(userService, times(1)).create(any(UserDTO.class));
        verify(assembler, times(1)).toModel(createdUser);
    }

    @Test
    void testGetUser() throws Exception {
        UserDTO user = new UserDTO(1, "John Doe", "john@example.com", 10, LocalDateTime.now());

        when(userService.getById(1)).thenReturn(user);
        when(assembler.toModel(user)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).getById(1);
        verify(assembler, times(1)).toModel(user);
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO updatedUser = new UserDTO(1, "John Smith", "johnsmith@example.com", 10, LocalDateTime.now());

        when(userService.update(eq(1), any(UserDTO.class))).thenReturn(updatedUser);
        when(assembler.toModel(updatedUser)).thenReturn(updatedUser); // <--- ДОБАВЬ

        mockMvc.perform(put("/users/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"johnsmith@example.com\",\"age\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("johnsmith@example.com"))
                .andExpect(jsonPath("$.age").value(10));

        verify(userService, times(1)).update(eq(1), any(UserDTO.class));
        verify(assembler, times(1)).toModel(updatedUser);
    }

}
