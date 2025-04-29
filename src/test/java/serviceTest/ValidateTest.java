package serviceTest;

import jakarta.validation.ValidationException;
import org.aston.model.UserModel;
import org.aston.util.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static util.TestObjectFactory.createDefaultUser;

/**
 * Тесты для класса Validate.
 */
class ValidateTest {

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
