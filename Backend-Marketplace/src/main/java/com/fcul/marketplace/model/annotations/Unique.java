package com.fcul.marketplace.model.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message() default "Duplicate value";
    String parameterName() default "";
    String className() default "";
}