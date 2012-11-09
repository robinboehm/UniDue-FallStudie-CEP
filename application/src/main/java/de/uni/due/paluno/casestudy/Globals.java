package de.uni.due.paluno.casestudy;

import java.util.Date;

public final class Globals {

	// Application context
	public static String IC_SERVICE_OBJECT = "IC_SERVICE_OBJECT";

	// Esper entities
	public static String E_TEMPERATURE_ENTITY = "Temperature";
	public static final String E_ROUTE_ENTITY = "Route";

	// Events
	public static final String E_EVENT_WAYPOINT_MAX_EXCEEDED = "E_EVENT_WAYPOINT_MAX_EXCEEDED";
	public static final String E_EVENT_ROUTE_AVERAGE_EXCEEDED = "E_EVENT_ROUTE_AVERAGE_EXCEEDED";
	public static final String E_EVENT_WAYPOINT_MAX_NOT_EXCEEDED = "E_EVENT_WAYPOINT_MAX_NOT_EXCEEDED";
	public static final String E_EVENT_ROUTE_AVERAGE_NOT_EXCEEDED = "E_EVENT_ROUTE_AVERAGE_NOT_EXCEEDED";

	// COSM
	public static final String COSM_API = "http://api.cosm.com/v2";
	public static final String API_URL = "ws://api.cosm.com:8080";
	public static final String API_KEY = "5T64pgQVJiKlfgQU2Q9IvH_UyUKSAKxTNjZma1kyQnFsQT0g";

	// Temperature
	public static final Double MAXIMUM_WAYPOINT_TEMPERATURE = 40.0;
	public static final Double MAXIMUM_AVERAGE = 30.0;

	// Helper methods
	public static void dump(Class<?> c, String msg) {
		System.out.println("[" + (new Date()).toString() + "]["
				+ c.getSimpleName() + "] " + msg);
	}
}