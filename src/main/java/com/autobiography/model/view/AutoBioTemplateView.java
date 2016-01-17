package com.autobiography.model.view;

/**
 * Author Dmitriy Liandres
 * Date 17.01.2016
 */
public class AutoBioTemplateView {
    private long id;
    private String name;

    public AutoBioTemplateView() {
    }

    public AutoBioTemplateView(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
