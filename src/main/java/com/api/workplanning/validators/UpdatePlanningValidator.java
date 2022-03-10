package com.api.workplanning.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class UpdatePlanningValidator extends PlanningValidator {

	@Override
	protected void checkMandatoryFields(Object target, Errors errors) {
		super.checkMandatoryFields(target, errors);
		ValidationUtils.rejectIfEmpty(errors, "id", "id.empty", "id field should be supplied");
	}
}
