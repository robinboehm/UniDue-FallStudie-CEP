package de.uni.due.paluno.casestudy.cosm;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.due.paluno.casestudy.cep.events.Event;
import de.uni.due.paluno.casestudy.cep.events.simple.WaypointTemperatureEvent;
import de.uni.due.paluno.casestudy.cosm.model.cosm.COSMDataStreamBody;

import java.io.IOException;

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

    public Object getObjectFromJson(final String jsonMessage, Class objectClass) {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        Object response = null;
        try {
            response = objectMapper.readValue(jsonMessage, objectClass);
        } catch (IOException e) {
            System.out.println(e);
        }
        return response;
    }


    public Event createEvent(COSMDataStreamBody dataStreamBody, String steamName) {
        Event event = new WaypointTemperatureEvent();
        event.setData(dataStreamBody.getCurrent_value());
        // TODO: Implement a unique identifier for a Event-Target
        String tags = "";
        if (dataStreamBody.getTags() != null) {
            for (String tag : dataStreamBody.getTags()) {
                tags += tag;
            }
        }
        event.setTarget(steamName + ";" + dataStreamBody.getId() + ";" + tags);
        return event;
    }
}
