import org.aston.homework_aston_consumer.dto.OperationType;
import org.aston.homework_aston_consumer.dto.UserEvent;
import org.aston.homework_aston_consumer.mapper.EmailMapper;
import org.aston.homework_aston_consumer.model.EmailModel;
import org.aston.homework_aston_consumer.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceKafkaConsumerTest {
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
    }

    @Test
    void testSendEmailOnCreate() {
        UserEvent event = new UserEvent(1, "ivan@example.com", OperationType.CREATE);
        EmailModel email = EmailMapper.toEmail(event);
        emailService.sendEmail(email);

        assertThat(email.getRecipient()).isEqualTo("ivan@example.com");
        assertThat(email.getSubject()).contains("Добро пожаловать");
        assertThat(email.getBody()).contains("был создан");
    }

    @Test
    void testSendEmailOnDelete() {
        UserEvent event = new UserEvent(2, "petr@example.com", OperationType.DELETE);

        EmailModel email = EmailMapper.toEmail(event);
        emailService.sendEmail(email);

        assertThat(email.getRecipient()).isEqualTo("petr@example.com");
        assertThat(email.getSubject()).contains("Удаление");
        assertThat(email.getBody()).contains("был удалён");
    }
}
