package com.autobiography.views;

import com.autobiography.model.db.Person;
import io.dropwizard.views.View;
import org.apache.shiro.SecurityUtils;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
public class GenericView extends View {

    public static final String BASE_FTL = "base.ftl";
    public static final String LOGIN_FTL = "login.ftl";
    public static final String MAIN_FTL = "main.ftl";
    public static final String PROFILE_FTL = "profile.ftl";

    public GenericView(String templateName) {
        super(templateName);
    }

    public String getUsername() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            Person principal = (Person) SecurityUtils.getSubject().getPrincipal();
            return principal != null ? principal.getUsername() : null;
        } else {
            return null;
        }

    }
}
