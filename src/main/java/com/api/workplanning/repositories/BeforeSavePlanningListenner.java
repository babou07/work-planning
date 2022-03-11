package com.api.workplanning.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.api.workplanning.model.Planning;

@Component
public class BeforeSavePlanningListenner extends AbstractMongoEventListener<Planning> {

	@Override
	public void onBeforeConvert(BeforeConvertEvent<Planning> event) {
		Planning planning = event.getSource();
		planning.setShifHash(Planning.generateShiftHash(planning));
	}

	@Override
	public void onAfterSave(AfterSaveEvent<Planning> event) {
		getLogger().info("save document : {}", event.getSource().toString());
	}

	private Logger getLogger() {
		return LoggerFactory.getLogger(BeforeSavePlanningListenner.class);
	}
}
