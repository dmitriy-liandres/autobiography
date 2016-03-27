package com.autobiography.model.view;

/**
 * Author Dmitriy Liandres
 * Date 27.03.2016
 */
public class AutoBioTextView {
    private String text;

    public AutoBioTextView() {
    }

    public AutoBioTextView(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
