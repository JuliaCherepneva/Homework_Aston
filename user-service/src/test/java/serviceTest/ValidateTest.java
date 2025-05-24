package serviceTest;

import jakarta.validation.ValidationException;
import org.aston.dto.UserDTO;
import org.aston.util.Validate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static util.TestObjectFactory.bigStringForValidNameTest;
import static util.TestObjectFactory.createDefaultUser;

/**
 * Тесты для класса Validate.
 */
class ValidateTest {

    @ParameterizedTest
    @ValueSource(strings = {"plainstring", "noatsymbol.com", ""})

    @DisplayName("Тест проверки валидации поля email у UserDTO при некорректных значения - отсутсвие символа @")
    void testInvalidEmail(String email) {
        UserDTO validUser = createDefaultUser();
        validUser.setEmail(email);
        ValidationException ex = assertThrows(ValidationException.class, () ->
                Validate.validateUser(validUser)
        );
        assertEquals("Строка не соответствует формату Email и не содержит символа @", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"n", bigStringForValidNameTest})
    @DisplayName("Тест проверки валидации поля name у UserDTO при некорректных значения - недопустимая длина name")
    void testInvalidName(String name) {
        UserDTO validUser = createDefaultUser();
        validUser.setName(name);
        ValidationException ex = assertThrows(ValidationException.class, () ->
                Validate.validateUser(validUser)
        );
        assertEquals("Имя пользователя должно содержать от 3 до 100 символов", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 0, 7, 100})
    @DisplayName("Тест проверки валидации поля age у UserDTO при некорректных значения - число вне диапазона 8 и 99")
    void testInvalidName(Integer age) {
        UserDTO validUser = createDefaultUser();
        validUser.setAge(age);
        ValidationException ex = assertThrows(ValidationException.class, () ->
                Validate.validateUser(validUser)
        );
        assertEquals("Возраст пользователя не должен быть меньше 8 и больше 99 лет", ex.getMessage());
    }

    @Test
    @DisplayName("Тест проверки валидации поля createdAt у UserDTO при некорректных значения - пользователь создан в будущем")
    void testInvalidLocalDataTime() {
        UserDTO validUser = createDefaultUser();
        validUser.setCreatedAt(LocalDateTime.now().plusDays(1));
        ValidationException ex = assertThrows(ValidationException.class, () ->
                Validate.validateUser(validUser)
        );
        assertEquals("Дата создания пользователя не может быть в будущем", ex.getMessage());
    }

}
