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
public class MessageProvider {

    private static final Map<Locale, Properties> messagesPerLocation = new HashMap<>();


    public static String message(String messageKey) throws IOException {
        Locale locale = Locale.getDefault();
        Properties propertiesWithMessages = messagesPerLocation.get(locale);
        if (propertiesWithMessages == null) {
            //lazy initialization
            synchronized (messagesPerLocation) {
                if (propertiesWithMessages == null) {
                    propertiesWithMessages = new Properties();
                    InputStream in = MessageProvider.class.getResourceAsStream("../messages_" + locale.toString() + ".properties");
                    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                    propertiesWithMessages.load(isr);
                    in.close();
                    messagesPerLocation.put(locale, propertiesWithMessages);
                }
            }
        }
        return (String) propertiesWithMessages.get(messageKey);

    }
}
