package de.uni.due.paluno.casestudy.services.cosm;

import java.util.concurrent.Future;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import de.uni.due.paluno.casestudy.Globals;
import de.uni.due.paluno.casestudy.services.cosm.model.cosm.COSMResponseBody;

public class COSM {
	public Double getTemperatureForWayPoint(String id) {
		Double value = null;
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Response> f = null;
		try {
			f = asyncHttpClient
					.prepareGet(Globals.COSM_API + "/feeds/" + id + ".json")
					.addQueryParameter("key", Globals.API_KEY).execute();

			Response r = f.get();

			COSMHelper helper = new COSMHelper();
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
}