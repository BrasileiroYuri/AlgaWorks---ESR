package com.algaworks.algafood.core.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.algaworks.algafood.core.validation.NomeadorValidator;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { NomeadorValidator.class })
public @interface Nome {

	String message() default "{0} precisa ter no máximo {1} caracteres.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int numero();

}
