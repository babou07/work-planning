package com.api.workplanning.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Planning {

	@Id
	private String id;
	private Worker worker;
	private Shift shift;

	/**
	 * Internal id provided by application to ensure one shift is unique per worker
	 * / per day
	 */
	@Indexed(unique = true)
	private int shifHash;

	public static int generateShiftHash(Planning pl) {
		return Objects.hash(pl.getWorker(), pl.getShift().getStart().getYear(),
				pl.getShift().getStart().getDayOfYear());
	}

}
