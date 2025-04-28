package serviceTest;

import jakarta.validation.ValidationException;
import org.aston.model.UserModel;
import org.aston.util.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static util.TestObjectFactory.createDefaultUser;


public class ValidateTest {
    // Тесты для проверки метода валидации.Моки здесь по сути не нужны,мы не обращаемся к БД
    // и отлавливаем ошибки на этапе создания объекта до того как отработает Hibernate
    @Test
    @DisplayName("Тест успешной валидации валидного UserModel")
    void validateUserShouldPassWhenValid() {
        UserModel validUser = createDefaultUser();
        assertDoesNotThrow(() -> Validate.validateUser(validUser));
    }

    @Test
    @DisplayName("Тест провала валидации UserModel при пустом имени")
    void validateUserShouldFailWhenNameIsBlank() {
        UserModel invalidUser = createDefaultUser();
        invalidUser.setName("");
        assertThrows(ValidationException.class, () -> Validate.validateUser(invalidUser));
    }

    @Test
    @DisplayName("Тест провала валидации UserModel при некорректной почте")
    void validateUserShouldFailWhenEmailIsInvalid() {
        UserModel invalidUser = createDefaultUser();
        invalidUser.setEmail("invalid-email");
        assertThrows(ValidationException.class, () -> Validate.validateUser(invalidUser));
    }

    @Test
    @DisplayName("Тест провала валидации UserModel при неправильном возрасте")
    void validateUserShouldFailWhenAgeIsTooLow() {
        UserModel invalidUser = createDefaultUser();
        invalidUser.setAge(5);
        assertThrows(ValidationException.class, () -> Validate.validateUser(invalidUser));
    }
}
