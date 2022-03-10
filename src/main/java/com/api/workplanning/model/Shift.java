package com.api.workplanning.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

/**
 * @author babel
 * 
 */
@Data
public class Shift {

	// Start date and time for a worker shift
	// expecting iso format : "yyyy-MM-ddTHH:mm:ss"
	private LocalDateTime start;

	// end date and time for a worker shift
	// expecting iso format : "yyyy-MM-ddTHH:mm:ss"
	private LocalDateTime end;
}
