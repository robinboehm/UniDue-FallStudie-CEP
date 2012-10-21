package de.uni.due.paluno.casestudy.cep.events;

public abstract class Event {
    private String target;
    private Double data;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Double getData() {
        return data;
    }

    public void setData(Double data) {
        this.data = data;
    }
}
