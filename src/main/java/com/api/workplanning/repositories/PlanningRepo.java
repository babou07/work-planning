package com.api.workplanning.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.workplanning.model.Planning;

public interface PlanningRepo extends MongoRepository<Planning, String> {

}
