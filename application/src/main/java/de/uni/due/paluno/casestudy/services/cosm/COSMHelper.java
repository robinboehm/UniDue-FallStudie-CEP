package de.uni.due.paluno.casestudy.services.cosm;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni.due.paluno.casestudy.services.cep.events.Event;
import de.uni.due.paluno.casestudy.services.cep.events.UncontrolledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMDataStreamBody;

public class COSMHelper {

	public String getAsAsJSON(final Object obj) {
		ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
		String objAsJson = null;

		try {
			objAsJson = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		return objAsJson;
	}

	public Object getObjectFromJson(final String jsonMessage,
			Class<?> objectClass) {
		ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
		Object response = null;
		try {
			response = objectMapper.readValue(jsonMessage, objectClass);
		} catch (IOException e) {
			System.out.println(e);
		}
		return response;
	}

	public Event createEvent(COSMDataStreamBody dataStreamBody, int streamId) {
		Event event = new UncontrolledWaypointTemperatureUpdate();
		event.setData(dataStreamBody.getCurrent_value());
		event.setTarget(Integer.toString(streamId));
		return event;
	}
}
