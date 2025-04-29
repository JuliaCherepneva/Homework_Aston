package org.aston.util;

import jakarta.validation.*;
import org.aston.model.UserModel;

import java.util.Set;

public class Validate {
    // Добавил валидацию через Bean validation такой подход показался лучше,
    // чем у меня самого до этого было во втором дз через if
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    public static void validateUser(UserModel userModel) {
        Set<ConstraintViolation<UserModel>> validationError = validator.validate(userModel);
        if (!validationError.isEmpty()) {
            validationError.forEach(error -> {
                System.out.println("Ошибка: " + error.getMessage());
                System.out.println("Поле: " + error.getPropertyPath());
                // вот тут кстати можно на логи заменить
            });
            throw new ValidationException("Данные не прошли проверку валидации");
        }
    }

}

