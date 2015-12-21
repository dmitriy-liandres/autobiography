package com.autobiography.views;

import com.autobiography.model.db.Person;
import com.autobiography.model.db.Profile;
import io.dropwizard.views.View;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
public class GenericView extends View {

    public static final String BASE_FTL = "base.ftl";
    public static final String LOGIN_FTL = "login.ftl";
    public static final String MAIN_FTL = "main.ftl";
    public static final String PROFILE_FTL = "profile.ftl";
    public static final String NOT_FOUND_FTL = "not-found.ftl";
    public static final String NOT_AUTHORIZED_FTL = "not-authorized.ftl";
    private static Map<Locale, Properties> messagesPerLocation = new HashMap<>();

    public GenericView(String templateName) {
        super(templateName, Charset.forName("UTF-8"));

    }

    public String getUsername() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            Person principal = (Person) SecurityUtils.getSubject().getPrincipal();
            return principal != null ? principal.getUsername() : null;
        } else {
            return null;
        }
    }

    public String getFullName() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            Profile profile = (Profile) SecurityUtils.getSubject().getSession().getAttribute("profile");
            if (profile != null) {
                return StringUtils.join(Arrays.asList(profile.getName(), profile.getSurname()), " ");
            }
        }
        return null;

    }

    public String message(String messageKey) throws IOException {
        Locale locale = Locale.getDefault();
        Properties propertiesWithMessages = messagesPerLocation.get(locale);
        if (propertiesWithMessages == null) {
            //lazy initialization

            propertiesWithMessages = new Properties();
            InputStream in = getClass().getResourceAsStream("../messages_" + locale.toString()+ ".properties");
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            propertiesWithMessages.load(isr);
            in.close();
            messagesPerLocation.put(locale, propertiesWithMessages);
        }
        return (String) propertiesWithMessages.get(messageKey);

    }
}
