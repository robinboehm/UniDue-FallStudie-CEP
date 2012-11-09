package de.uni.due.paluno.temperatureFeeder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import Pachube.Pachube;
import Pachube.PachubeException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TemperatureFeeder {

	private static String COSM_KEY = "5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g";

	public static void main(String[] args) throws PachubeException,
			InterruptedException {
		Pachube api = new Pachube(COSM_KEY);

		List<Feed> feeds = getFeeds("futurelogistics");

		while (true) {
			Iterator<Feed> i = feeds.iterator();
			while (i.hasNext()) {
				Feed feed = i.next();

				Double temperature = Double.parseDouble(getTemperature(
						feed.getLat(), feed.getLon()));

				api.getFeed(feed.getId()).updateDatastream(0, temperature);
			}

			// 60 Sekunden schlafen
			Thread.sleep(1000 * 60);
		}
	}

	private static String getTemperature(String latitude, String longtitude) {
		Client client = Client.create();

		WebResource webResource = client
				.resource("http://free.worldweatheronline.com/feed/weather.ashx?q="
						+ latitude
						+ ","
						+ longtitude
						+ "&format=json&num_of_days=2&key=6ae66ed2bb075138120911");

		String s = webResource.get(String.class);

		ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

		try {
			JsonNode data = objectMapper.readTree(s);
			JsonNode conditions = data.get("data").get("current_condition");

			return conditions.findValue("temp_C").textValue();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<Feed> getFeeds(String userName) {
		List<Feed> feeds = new ArrayList<Feed>();

		Client client = Client.create();
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("user", "futurelogistics");
		WebResource webResource = client
				.resource("http://api.cosm.com/v2/feeds.json?user=" + userName);
		String s = webResource.queryParams(queryParams)
				.header("X-ApiKey", COSM_KEY).get(String.class);

		ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

		try {
			JsonNode data = objectMapper.readTree(s);
			JsonNode results = data.get("results");

			Iterator<JsonNode> i = results.iterator();
			while (i.hasNext()) {
				JsonNode result = i.next();

				Feed f = new Feed();
				f.setId(result.get("id").asInt());
				f.setLat(result.get("location").get("lat").toString());
				f.setLon(result.get("location").get("lon").toString());

				feeds.add(f);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return feeds;
	}
}
