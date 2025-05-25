package org.aston.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.aston.exception.model.Code;

@EqualsAndHashCode(callSuper = false)
@Data
public class ValidationException extends RuntimeException {

    private final Code code;

    public ValidationException(String message) {
        super(message);
        this.code = Code.VALIDATION_ERROR;
    }
}
