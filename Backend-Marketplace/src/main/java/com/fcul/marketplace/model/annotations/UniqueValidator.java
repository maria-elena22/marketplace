package com.fcul.marketplace.model.annotations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;
    private String fieldName;
    private String message;
    private String tableName;


    @Override
    public void initialize(Unique constraintAnnotation) {
        fieldName = constraintAnnotation.parameterName();
        message = constraintAnnotation.message();
        tableName = constraintAnnotation.className();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        String jpql = "SELECT COUNT(e) FROM " + tableName + " e WHERE e." + fieldName + " = :value";
        Query query = entityManager.createQuery(jpql).setParameter("value", value);
        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}