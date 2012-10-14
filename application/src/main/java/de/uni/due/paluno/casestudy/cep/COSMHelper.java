package de.uni.due.paluno.casestudy.cep;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni.due.paluno.casestudy.cep.model.Measurement;
import de.uni.due.paluno.casestudy.cep.model.cosm.COSMServerResponse;

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


    public Measurement createMeasurement(COSMServerResponse response) {
        Measurement measurement = new Measurement();
        measurement.setId(response.getBody().getId());
        measurement.setUnit(response.getBody().getUnit().getLabel());
        measurement.setValue(response.getBody().getCurrent_value());
        return measurement;
    }
}
