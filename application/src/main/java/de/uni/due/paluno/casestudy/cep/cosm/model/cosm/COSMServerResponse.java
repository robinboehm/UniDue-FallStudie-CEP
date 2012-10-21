package de.uni.due.paluno.casestudy.cep.cosm.model.cosm;

public class COSMServerResponse {
    private COSMResponseBody body;
    private String resource;

    public COSMServerResponse() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public COSMResponseBody getBody() {
        return body;
    }

    public void setBody(COSMResponseBody body) {
        this.body = body;
    }
}
