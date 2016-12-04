package com.autobiography.resources.tires.model;

/**
 * Author Dmitriy Liandres
 * Date 25.11.2016
 */
public class TiresResultItem {
    private TiresJsonItem diameter;
    private TiresJsonItem aspectRatio;
    private TiresJsonItem nominalWidth;

    private String producer;
    private String description;
    private String type;
    private String speedSymbol;
    private String countryOfOrigin;
    private String price;
    private String town;
    private String phoneNumber;

    public TiresJsonItem getDiameter() {
        return diameter;
    }

    public void setDiameter(TiresJsonItem diameter) {
        this.diameter = diameter;
    }

    public TiresJsonItem getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(TiresJsonItem aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public TiresJsonItem getNominalWidth() {
        return nominalWidth;
    }

    public void setNominalWidth(TiresJsonItem nominalWidth) {
        this.nominalWidth = nominalWidth;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpeedSymbol() {
        return speedSymbol;
    }

    public void setSpeedSymbol(String speedSymbol) {
        this.speedSymbol = speedSymbol;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return diameter.getText() + ";" + aspectRatio.getText() + ";" + nominalWidth.getText() + ";" + producer + ";" + description + ";" + type + ";" + speedSymbol + ";" + countryOfOrigin + ";" + price + ";" + town + ";" + phoneNumber + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TiresResultItem)) return false;

        TiresResultItem that = (TiresResultItem) o;

        if (diameter != null ? !diameter.equals(that.diameter) : that.diameter != null) return false;
        if (aspectRatio != null ? !aspectRatio.equals(that.aspectRatio) : that.aspectRatio != null) return false;
        if (nominalWidth != null ? !nominalWidth.equals(that.nominalWidth) : that.nominalWidth != null) return false;
        if (producer != null ? !producer.equals(that.producer) : that.producer != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (speedSymbol != null ? !speedSymbol.equals(that.speedSymbol) : that.speedSymbol != null) return false;
        if (countryOfOrigin != null ? !countryOfOrigin.equals(that.countryOfOrigin) : that.countryOfOrigin != null)
            return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (town != null ? !town.equals(that.town) : that.town != null) return false;
        return !(phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null);

    }

    @Override
    public int hashCode() {
        int result = diameter != null ? diameter.hashCode() : 0;
        result = 31 * result + (aspectRatio != null ? aspectRatio.hashCode() : 0);
        result = 31 * result + (nominalWidth != null ? nominalWidth.hashCode() : 0);
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (speedSymbol != null ? speedSymbol.hashCode() : 0);
        result = 31 * result + (countryOfOrigin != null ? countryOfOrigin.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (town != null ? town.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
