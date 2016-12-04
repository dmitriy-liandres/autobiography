package com.autobiography.resources.tires.model;

import java.util.List;

/**
 * Author Dmitriy Liandres
 * Date 25.11.2016
 */
public class TiresJsonResponse {

    private List<TiresJsonItem> result;

    public List<TiresJsonItem> getResult() {
        return result;
    }

    public void setResult(List<TiresJsonItem> result) {
        this.result = result;
    }
}
