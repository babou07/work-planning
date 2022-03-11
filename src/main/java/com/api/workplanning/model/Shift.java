package com.api.workplanning.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author babel
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shift {

	// Start date and time for a worker shift
	// expecting iso format : "yyyy-MM-ddTHH:mm:ss"
	private LocalDateTime start;

	// end date and time for a worker shift
	// expecting iso format : "yyyy-MM-ddTHH:mm:ss"
	private LocalDateTime end;
}
