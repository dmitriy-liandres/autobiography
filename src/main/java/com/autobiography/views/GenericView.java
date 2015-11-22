package com.autobiography.views;

import io.dropwizard.views.View;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
public class GenericView extends View {

    protected GenericView(String templateName) {
        super(templateName);
    }

    public String getUsername() {
//        if (SecurityUtils.getSubject().isAuthenticated()) {
//            Object principal = SecurityUtils.getSubject().getPrincipal();
//            return principal != null ? principal.toString() : null;
//        } else {
//            return null;
//        }
        return "ok";//todo

    }
}
