package com.api.workplanning.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Planning {
	
	@Id
	private String id;
	private Worker worker;
	private Shift shift;
	
	/**
	 * Internal id provided by application to ensure one shift is unique per worker / per day
	 */
	@Indexed(unique=true)
	private int shifHash;
	
	public int generateShiftHash() {
		return Objects.hash(this.getWorker(), this.getShift().getStart().getYear(), this.getShift().getStart().getDayOfYear());
	}
	
}
