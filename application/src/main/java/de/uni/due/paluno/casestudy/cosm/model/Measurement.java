package de.uni.due.paluno.casestudy.cosm.model;

import java.util.Date;

public class Measurement {
    private String id;
    private String unit;
    private double value;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Measurement() {/* Object-Mapper */}

    public void setId(String id) {
        this.id = id;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id='" + id + '\'' +
                ", unit=" + unit +
                ", value=" + value +
                '}';
    }
}
