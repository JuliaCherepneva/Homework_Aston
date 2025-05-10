package serviceTest;

import org.aston.dto.OperationType;
import org.aston.dto.UserDTO;
import org.aston.dto.UserEvent;
import org.aston.model.UserModel;
import org.aston.repository.UserRepository;
import org.aston.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceKafkaTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateSendsKafkaEvent() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Иван");
        userDTO.setEmail("ivan@example.com");

        UserModel savedUser = new UserModel();
        savedUser.setId(1);
        savedUser.setName("Иван");
        savedUser.setEmail("ivan@example.com");
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);
        userService.create(userDTO);
        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);

        verify(kafkaTemplate).send(eq("user-events"), eventCaptor.capture());

        UserEvent event = eventCaptor.getValue();
        assertThat(event).isNotNull();
        assertThat(event.getUserId()).isEqualTo(savedUser.getId());
        assertThat(event.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(event.getOperationType()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void testDeleteSendsKafkaEvent() {
        int userId = 5;
        UserModel user = new UserModel();
        user.setId(userId);
        user.setName("Петр");
        user.setEmail("petr@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).deleteById(userId);

        ArgumentCaptor<UserEvent> eventCaptor = ArgumentCaptor.forClass(UserEvent.class);
        verify(kafkaTemplate).send(eq("user-events"), eventCaptor.capture());

        UserEvent event = eventCaptor.getValue();
        assertThat(event).isNotNull();
        assertThat(event.getUserId()).isEqualTo(userId);
        assertThat(event.getEmail()).isEqualTo("petr@example.com");
        assertThat(event.getOperationType()).isEqualTo(OperationType.DELETE);
    }
}
