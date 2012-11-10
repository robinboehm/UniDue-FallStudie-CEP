package de.uni.due.paluno.casestudy.services.cosm.model.cosm;

public class COSMUnit {
    private String symbol;
    private String label;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public COSMUnit() {/* Object-Mapper */}

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
