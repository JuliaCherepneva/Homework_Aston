package org.aston.util;

import jakarta.validation.*;
import org.aston.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Validate {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    private static final Logger logger = LoggerFactory.getLogger(Validate.class);

    private Validate() {
    }

    public static void validateUser(UserModel userModel) {
        Set<ConstraintViolation<UserModel>> validationError = validator.validate(userModel);
        if (!validationError.isEmpty()) {
            validationError.forEach(error -> {
                logger.error("Ошибка: {}", error.getMessage());
                logger.error("Поле: {}", error.getPropertyPath());
            });
            throw new ValidationException("Данные не прошли проверку валидации");
        }
    }
}

