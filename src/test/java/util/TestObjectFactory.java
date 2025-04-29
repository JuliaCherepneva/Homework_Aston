package util;

import org.aston.model.UserModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class TestObjectFactory {
    // Фабрика для создания объектов для тестов,что бы в тестовых классах по сути были только тесты
    // и вызов методов для создания объектов
    private static final Random random = new Random();
    private TestObjectFactory() {
        throw new UnsupportedOperationException("Хахахахахахааххаахахах");
    }
    public static UserModel createDefaultUser() {
        return UserModel.builder()
                .name("Default Name")
                .email("default@example.com")
                .age(30)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static UserModel createUserWithCustomName(String name) {
        return UserModel.builder()
                .name(name)
                .email(name.toLowerCase().replace(" ", ".") + "@example.com")
                .age(25)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static UserModel createUserWithId(int id) {
        UserModel user = createDefaultUser();
        user.setId(id);
        return user;
    }
    public static UserModel createRandomUser() {
        String name = "User" + UUID.randomUUID().toString().substring(0, 8);
        return UserModel.builder()
                .name(name)
                .email(name.toLowerCase() + "@example.com")
                .age(18 + random.nextInt(50))
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static List<UserModel> createRandomUsers(int count) {
        return java.util.stream.Stream.generate(TestObjectFactory::createRandomUser)
                .limit(count)
                .toList();
    }
}
