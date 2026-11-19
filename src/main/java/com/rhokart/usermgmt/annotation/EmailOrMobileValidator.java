package com.rhokart.usermgmt.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.rhokart.usermgmt.dto.UserKeyInfo;

public class EmailOrMobileValidator implements ConstraintValidator<EmailOrMobile, UserKeyInfo> {

    @Override
    public boolean isValid(UserKeyInfo dto, ConstraintValidatorContext context) {
        boolean isEmailValid = dto.getEmail() != null && dto.getMobile() ==null && dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");
        boolean isMobileValid = dto.getMobile() != null && dto.getEmail() == null&& dto.getMobile().matches("^[0-9]{10}$");
        if (!isEmailValid && !isMobileValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Either email or mobile must be provided")
                    .addConstraintViolation();
        }
        return isEmailValid || isMobileValid;
    }

    @Override
    public void initialize(EmailOrMobile constraintAnnotation) {
    }
}
