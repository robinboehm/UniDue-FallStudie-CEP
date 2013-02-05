package de.uni.due.paluno.casestudy.services.cosm;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.model.Route;
import de.uni.due.paluno.casestudy.model.WayPoint;
import de.uni.due.paluno.casestudy.services.cep.events.Event;
import de.uni.due.paluno.casestudy.services.cep.events.UncontrolledWaypointTemperatureUpdate;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMDataStreamBody;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

public class COSMWebSocketHelper {

    // Transformations
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


    // WebSocket Helper
    public Double getTemperatureForWayPoint(String id) {
        Double value = null;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f = null;
        try {
            f = asyncHttpClient
                    .prepareGet(Globals.COSM_API + "/feeds/" + id + ".json")
                    .addQueryParameter("key", Globals.API_KEY).execute();

            Response r = f.get();

            COSMWebSocketHelper helper = new COSMWebSocketHelper();
            COSMResponseBody responseBody = (COSMResponseBody) helper
                    .getObjectFromJson(r.getResponseBody(),
                            COSMResponseBody.class);

            value = responseBody.getDatastreams()[0].getCurrent_value();

        } catch (Exception e) {
            e.printStackTrace();
        }

        asyncHttpClient.close();

        return value;
    }

    public Map<String, Double> getTemperaturesForRoute(Route route) {
        Map<String, Double> temps = new HashMap<String, Double>();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        Future<Response> f = null;

        Iterator<WayPoint> i = route.getPoints().iterator();
        while (i.hasNext()) {
            WayPoint wp = i.next();

            try {
                f = asyncHttpClient
                        .prepareGet(
                                Globals.COSM_API + "/feeds/" + wp.getId()
                                        + ".json")
                        .addQueryParameter("key", Globals.API_KEY).execute();

                Response r = f.get();

                COSMWebSocketHelper helper = new COSMWebSocketHelper();
                COSMResponseBody responseBody = (COSMResponseBody) helper
                        .getObjectFromJson(r.getResponseBody(),
                                COSMResponseBody.class);

                temps.put(wp.getId(),
                        responseBody.getDatastreams()[0].getCurrent_value());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        asyncHttpClient.close();

        return temps;
    }
}
