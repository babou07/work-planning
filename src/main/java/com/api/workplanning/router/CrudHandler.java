package com.api.workplanning.router;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.api.workplanning.model.Planning;
import com.api.workplanning.repositories.PlanningRepo;
import com.api.workplanning.validators.PlanningValidator;
import com.api.workplanning.validators.UpdatePlanningValidator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CrudHandler {

	@Autowired
	private PlanningRepo repo;
	
	public Mono<ServerResponse> streamAll(ServerRequest request) {
		return ServerResponse.ok().body(Flux.just(repo.findAll()), Planning.class);
	}
	
	public Mono<ServerResponse> streamOne(ServerRequest request) {
		return ServerResponse.ok().body(Mono.just(repo.findById(request.pathVariable("id"))), Planning.class);
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		return ServerResponse.ok().body(Mono.fromCallable(() -> {
			repo.deleteById(request.pathVariable("id"));
			return "ok";
		}), String.class);
	}
	
	public Mono<ServerResponse> update(ServerRequest request) {
		return request.bodyToMono(Planning.class).flatMap(planning -> {
			getLogger().info("post planning request with body : ");
			validate(new UpdatePlanningValidator(), planning); // Fields validation
			return ServerResponse.ok().body(Mono.just(repo.save(planning)), Planning.class);
		});
	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Planning.class).flatMap(planning -> {
			validate(new PlanningValidator(), planning);  // Fields validation
			return ServerResponse.ok().body(Mono.just(save(planning)), Planning.class);
		});
	}
	
	private void validate(Validator validator, Planning planning) {
		Errors errors = new BeanPropertyBindingResult(planning, String.class.getName());
		validator.validate(planning, errors);
		if (errors == null || errors.getAllErrors().isEmpty()) {
			getLogger().info("planning validation ok");
		} else {
			String reasons = errors.getAllErrors().toString();
			getLogger().warn(reasons);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reasons);
		}
	}
	
	private Planning save(Planning planning) {
		try {
			return repo.save(planning);
		} catch (org.springframework.dao.DuplicateKeyException e) {
			// a duplicate may happen if a shift already exist in db for that specific worker and specific day
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "a shift already exists for worker " + planning.toString());
		}
	}
	
	protected Logger getLogger() {
		return LoggerFactory.getLogger(CrudHandler.class);
	}
}
