package com.api.workplanning.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;


@Configuration
public class RouterConfig {
	
	@Autowired
	CrudHandler crudHandler;

	@Bean
	public RouterFunction<?> zappalitoRouter() {
		return route(GET("/planning"), crudHandler::streamAll)
			.andRoute(GET("/planning/{id}"), crudHandler::streamOne)
			.andRoute(POST("/planning"), crudHandler::create)
			.andRoute(DELETE("/planning/{id}"), crudHandler::delete)
			.andRoute(PUT("/planning"), crudHandler::update);
	}
	
}
