package com.gamingzone.redeem;

public class Field {
    private String value;
    private String xpath;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String toString() {
        return "Field{" +
                "value='" + value + '\'' +
                ", xpath='" + xpath + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
