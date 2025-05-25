package util;

import org.aston.dto.UserDTO;
import org.aston.model.UserModel;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Фабрика для создания объектов для тестов.
 */
public final class TestObjectFactory {
    public static final String bigStringForValidNameTest = "abcdefghijklmnopqrstuwyxyz + " +
            "abcdefghijklmnopqrstuwyxyz + " +
            "abcdefghijklmnopqrstuwyxyz + " +
            "abcdefghijklmnopqrstuwyxyz + " +
            "abcdefghijklmnopqrstuwyxyz + ";

    private static final Random random = new Random();

    private TestObjectFactory() {
        throw new UnsupportedOperationException("Создание экземпляров TestObjectFactory невозможно.");
    }

    public static String invalidNames() {
        String name = "wrongName";
        return name.repeat(100);
    }

    public static UserDTO createDefaultUser() {
        return UserDTO.builder()
                .name("Default Name")
                .email("default@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
