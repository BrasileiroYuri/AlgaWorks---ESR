package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.algaworks.algafood.core.constraints.Nome;

public class NomeadorValidator implements ConstraintValidator<Nome, String> {

	private int value;

	@Override
	public void initialize(Nome constraintAnnotation) {
		this.value = constraintAnnotation.numero();
	}

	@Override
	public boolean isValid(String property, ConstraintValidatorContext context) {
		boolean valid = true;
		if (!property.equals(null)) {
			valid = property.length() < this.value;
		}
		return valid;
	}
}
