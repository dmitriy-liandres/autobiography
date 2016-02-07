package com.autobiography.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
public class MessageHelper {

    private static final Map<Locale, Properties> MESSAGES_PER_LOCATION = new HashMap<>();

    public static String message(String messageKey) throws IOException {
        Locale locale = Locale.getDefault();
        return message(messageKey, locale);
    }

    public static String message(String messageKey, Locale locale) throws IOException {
        Properties propertiesWithMessages = MESSAGES_PER_LOCATION.get(locale);
        if (propertiesWithMessages == null) {
            //lazy initialization
            synchronized (MESSAGES_PER_LOCATION) {
                if (MESSAGES_PER_LOCATION.get(locale) == null) {
                    propertiesWithMessages = new Properties();
                    InputStream in = null;
                    try {
                        in = MessageHelper.class.getResourceAsStream("../messages_" + locale.toString() + ".properties");
                        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                        propertiesWithMessages.load(isr);
                        MESSAGES_PER_LOCATION.put(locale, propertiesWithMessages);
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }
            }
        }
        return (String) MESSAGES_PER_LOCATION.get(locale).get(messageKey);

    }
}
