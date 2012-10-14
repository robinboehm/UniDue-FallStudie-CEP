package de.uni.due.paluno.casestudy.cep.model.cosm;

public class COSMUnit {
    private String symbol;
    private String label;

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
