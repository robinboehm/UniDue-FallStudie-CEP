package de.uni.due.paluno.casestudy.services.cosm.model.cosm;

import java.util.HashMap;
import java.util.Map;

public class COSMServerRequest {

    public static final String METHOD_SUBSCRIBE = "subscribe";

    private Map<String, Object> headers = new HashMap<String, Object>();
    private String method;
    private String resource;

    public COSMServerRequest() {
    }

    public COSMServerRequest(String method, String resource) {
        this.method = method;
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setAPIKey(String apiKey){
        headers.put("X-ApiKey", apiKey);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }
}
