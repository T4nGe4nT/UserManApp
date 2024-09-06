package org.example.usermanagementapplication.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // Custom username validation logic (e.g., alphanumeric, length)
        return username != null && username.matches("[a-zA-Z0-9]+") && username.length() >= 4;
    }
}
