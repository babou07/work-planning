package com.api.workplanning.workplanningapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.workplanning.model.Planning;
import com.api.workplanning.model.Shift;
import com.api.workplanning.model.Worker;
import com.api.workplanning.repositories.BeforeSavePlanningListenner;
import com.api.workplanning.repositories.PlanningRepo;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PlanningRepositoryTest {

	@Autowired
	private PlanningRepo repo;

	@Test
	public void saveOnePlanning() {

		repo.deleteAll();

		LocalDateTime start = LocalDateTime.of(2020, 1, 10, 8, 0);
		Shift shift = new Shift(start, start.plusMinutes(8 * 60));
		Planning planning = new Planning("1", new Worker("testFirstname", "testLastName"), shift, 0);

		MongoMappingEvent<Planning> event = new BeforeConvertEvent<Planning>(planning, "collection-1");
		BeforeSavePlanningListenner listener = new BeforeSavePlanningListenner();
		listener.onApplicationEvent(event);
		planning = repo.save(planning);

		// check mongoDB id
		assertNotNull(planning.getId());

		// check shift unique hash to ensure only one shift is allowed per day / worker
		assertEquals(Planning.generateShiftHash(planning), planning.getShifHash());
	}

}
