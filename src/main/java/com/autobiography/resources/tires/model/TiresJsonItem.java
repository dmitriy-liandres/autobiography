package com.autobiography.resources.tires.model;

/**
 * Author Dmitriy Liandres
 * Date 25.11.2016
 */
public class TiresJsonItem {
    private String val;
    private String text;

    public TiresJsonItem() {
    }

    public TiresJsonItem(String val, String text) {
        this.val = val;
        this.text = text;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TiresJsonItem)) return false;

        TiresJsonItem that = (TiresJsonItem) o;

        if (val != null ? !val.equals(that.val) : that.val != null) return false;
        return !(text != null ? !text.equals(that.text) : that.text != null);

    }

    @Override
    public int hashCode() {
        int result = val != null ? val.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TiresJsonItem{" +
                "val='" + val + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
