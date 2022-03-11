package com.api.workplanning.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.api.workplanning.model.Planning;

public class PlanningValidator implements Validator {
	
	// A shift must last 8 hours; value in minutes
	private static long SHIFT_NB_MINUTES = 8 * 60;

	@Override
	public boolean supports(Class<?> clazz) {
		return Planning.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// check if mandatory fields are supplied
		checkMandatoryFields(target, errors);
		Planning planning = (Planning) target;
		
		// 24 hours timetable 0-8,8-16,16-24
		// the start shift datetime should be either 0, 8 or 16
		int startHour = planning.getShift().getStart().getHour();
		int startMinute = planning.getShift().getStart().getMinute();
		if((startHour != 0 || startHour != 8 || startHour != 16) && startMinute != 0) {
			errors.rejectValue("shift.start", "shift.start.time", "shift.start time should be either 0:00, 8:00 or 16:00");
		}
		
		// A shift is 8 hours long
		if(!planning.getShift().getStart().plusMinutes(SHIFT_NB_MINUTES).equals(planning.getShift().getEnd())) {
			errors.rejectValue("shift.end", "shift.end.time", "shift.end time should be 8 hours later than start time");
		}
	}

	protected void checkMandatoryFields(Object target, Errors errors) {
		// all worker fields are mandatory
		ValidationUtils.rejectIfEmpty(errors, "worker", "worker.empty", "worker field should be supplied");
		ValidationUtils.rejectIfEmpty(errors, "worker.firstName", "worker.firstname.empty", "worker firstName is mandatory");
		ValidationUtils.rejectIfEmpty(errors, "worker.lastName", "worker.lastName.empty", "worker lastName is mandatory");

		// all shift fields are mandatory
		ValidationUtils.rejectIfEmpty(errors, "shift", "shif.empty", "shift field should be supplied");
		ValidationUtils.rejectIfEmpty(errors, "shift.start", "shift.start.empty", "shift start datetime is mandatory");
		ValidationUtils.rejectIfEmpty(errors, "shift.end", "shift.end.empty", "shift end time is mandatory");
	}

	protected Logger getLogger() {
		return LoggerFactory.getLogger(PlanningValidator.class);
	}

}
