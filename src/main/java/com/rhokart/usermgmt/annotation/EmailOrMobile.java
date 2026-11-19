package com.rhokart.usermgmt.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = EmailOrMobileValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrMobile {
    String message() default "Either email or mobile must be provided and valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
