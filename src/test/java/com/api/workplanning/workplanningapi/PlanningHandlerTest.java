package com.api.workplanning.workplanningapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.api.workplanning.model.Planning;
import com.api.workplanning.model.Shift;
import com.api.workplanning.model.Worker;
import com.api.workplanning.repositories.PlanningRepo;
import com.api.workplanning.router.CrudHandler;
import com.api.workplanning.router.RouterConfig;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = RouterConfig.class)
@Import(CrudHandler.class)
public class PlanningHandlerTest {
	
	@MockBean
	private PlanningRepo repo;

	@Autowired
	private WebTestClient webTestClient;



	@Test
	public void testCreatePlanning() {
		LocalDateTime start = LocalDateTime.of(2020, 1, 10, 8, 0);
		Shift shift = new Shift(start, start.plusMinutes(8 * 60)); // end shift = start time + 8 hours
		Planning planning = new Planning();
		planning.setId("1");
		planning.setWorker(new Worker("testFirstname", "testLastName"));
		planning.setShift(shift);
		
		when(repo.save(any())).thenReturn(planning);
		webTestClient.post()
			.uri("/planning")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(planning), Planning.class)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.jsonPath("id").isEqualTo("1")
			.jsonPath("worker.firstName").isEqualTo("testFirstname");

	}
	
	/**
	 * If a shift is not 8 hours long, a bad request status should be sent
	 */
	@Test
	public void testValidationFailOnShiftCheckNbHours() {
		LocalDateTime start = LocalDateTime.of(2020, 1, 10, 8, 0);
		Shift shift = new Shift(start, start.plusMinutes(6 * 60));  // end shift = start time + 6 hours only
		Planning planning = new Planning();
		planning.setId("1");
		planning.setWorker(new Worker("testFirstname", "testLastName"));
		planning.setShift(shift);
		
		when(repo.save(any())).thenReturn(planning);
		webTestClient.post()
			.uri("/planning")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(planning), Planning.class)
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody()
			.jsonPath("message").exists();

	}
	
	/**
	 * If incoming request does not supplied a worker firstName field, a bad request status should be sent
	 */
	@Test
	public void testMandatoryWorkerField() {
		LocalDateTime start = LocalDateTime.of(2020, 1, 10, 8, 0);
		Shift shift = new Shift(start, start.plusMinutes(8 * 60));
		Planning planning = new Planning();
		planning.setId("1");
		planning.setWorker(new Worker(null, "testLastName"));
		planning.setShift(shift);
		
		when(repo.save(any())).thenReturn(planning);
		webTestClient.post()
			.uri("/planning")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(planning), Planning.class)
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody()
			.jsonPath("message").value(val -> {
				String v = (String) val;
				assertTrue(v.contains("worker firstName is mandatory"));
			});


	}
	
	/**
	 * If incoming request does not supplied a shift end date field, a bad request status should be sent
	 */
	@Test
	public void testMandatoryShiftField() {
		LocalDateTime start = LocalDateTime.of(2020, 1, 10, 8, 0);
		Shift shift = new Shift(start, null);  // end shift date is not supplied
		Planning planning = new Planning();
		planning.setId("1");
		planning.setWorker(new Worker("testFirstName", "testLastName"));
		planning.setShift(shift);
		
		when(repo.save(any())).thenReturn(planning);
		webTestClient.post()
			.uri("/planning")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(planning), Planning.class)
			.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody()
			.jsonPath("message").value(val -> {
				String v = (String) val;
				assertTrue(v.contains("shift end time is mandatory"));
			});

	}
}
