package de.uni.due.paluno.casestudy.model;

import java.util.List;
import java.util.concurrent.Future;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.cosm.COSMHelper;
import de.uni.due.paluno.casestudy.cosm.model.cosm.COSMDataStreamBody;
import de.uni.due.paluno.casestudy.cosm.model.cosm.COSMResponseBody;

public class WorldHelper {

	public static World createWorldFromDataStreams(List<String> list) {
		World world = new World();

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		for (String feed : list) {
			Future<Response> f = null;
			try {
				f = asyncHttpClient.prepareGet(Globals.COSM_API + feed)
						.addQueryParameter("key", Globals.API_KEY).execute();

				Response r = f.get();

				COSMHelper helper = new COSMHelper();
				COSMResponseBody responseBody = (COSMResponseBody) helper
						.getObjectFromJson(r.getResponseBody(),
								COSMResponseBody.class);

				if (responseBody.getDescription().equals("Route")) {
					Route route = new Route(responseBody.getId() + "");
					for (COSMDataStreamBody dataStream : responseBody
							.getDatastreams()) {
						WayPoint wayPoint = new WayPoint(
								dataStream.getTags()[0]);
						wayPoint.setTemperature(dataStream.getCurrent_value());
						route.addWaypoint(wayPoint);
					}
					world.addRoute(route);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return world;
	}
}
