package com.autobiography.db.migration;

import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Author Dmitriy Liandres
 * Date 17.01.2016
 */
public class ImportAutoBioTemplates extends CustomTaskChangeBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Database database) throws CustomChangeException {
        logger.info("Start ImportAutoBioTemplates");
        Connection dbConnection = null;
        PreparedStatement psDeleteTemplate = null;
        PreparedStatement psInsertTemplate = null;
        //local - >Biography names
        Map<String, Set<String>> bios = new ConcurrentHashMap<>();

        try {
            dbConnection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();

            //delete old templates
            String sqlDeleteOld =
                    "delete from auto_bio_template";
            psDeleteTemplate = dbConnection.prepareStatement(sqlDeleteOld);
            psDeleteTemplate.executeUpdate();

            //insert new templates
            String sqlInsertTemplate =
                    "insert into auto_bio_template (locale, name, template)  values (?, ?, ?) ";
            psInsertTemplate = dbConnection.prepareStatement(sqlInsertTemplate);

            final String path = "examples/locales";
            final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

            if (jarFile.isFile()) {  // Run with JAR file
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) { //filter according to the path
                        String[] nameParts = name.split("/");
                        if (nameParts.length == 5 && nameParts[3].equals("biography-for-work")) {
                            bios.putIfAbsent(nameParts[2], new HashSet<>());
                            bios.get(nameParts[2]).add(nameParts[4]);
                        }
                    }
                }
                jar.close();
            } else { // Run with IDE
                final URL url = getClass().getResource("/" + path);

                File[] localeDirectories = new File(url.toURI()).listFiles();
                for (File localeDirectory : localeDirectories) {
                    File[] templates = localeDirectory.listFiles((dir, name) -> {
                        return "biography-for-work".equals(name);
                    })[0].listFiles();

                    String locale = localeDirectory.getName();
                    bios.putIfAbsent(locale, new HashSet<>());

                    if (templates != null && templates.length > 0) {
                        for (File templateDirectory : templates) {
                            bios.get(locale).add(templateDirectory.getName());
                        }
                    }
                }
            }

            //name1 = IOUtils.toString(getClass().getResourceAsStream("/examples/locales/en_US/biography-for-work/short-general/name.txt"));
            for (Map.Entry<String, Set<String>> templatesPerLocale : bios.entrySet()) {
                String locale = templatesPerLocale.getKey();
                for (String templateName : templatesPerLocale.getValue()) {
                    String pathToTemplate = "/" + path + "/" + locale + "/biography-for-work/" + templateName;
                    String name = loadFile(pathToTemplate + "/name.txt");
                    String template = loadFile(pathToTemplate + "/template.txt");
                    psInsertTemplate.setString(1, locale);
                    psInsertTemplate.setString(2, name);
                    psInsertTemplate.setString(3, template);
                    psInsertTemplate.executeUpdate();
                }
            }


        } catch (Exception t) {
            throw new CustomChangeException("Migration of ImportAutoBioTemplates failed!"
                    + "\n"
                    + "\nbios = " + bios
                    + "\n"
                    , t);
        } finally {
            if (psInsertTemplate != null) {
                try {
                    psInsertTemplate.close();
                } catch (SQLException e) {
                    throw new CustomChangeException(e);
                }
            }
            if (psDeleteTemplate != null) {
                try {
                    psDeleteTemplate.close();
                } catch (SQLException e) {
                    throw new CustomChangeException(e);
                }
            }

        }
    }

    private String loadFile(String path) throws IOException {
        char[] buffer = new char[10000];
        try (InputStream in = getClass().getResourceAsStream(path)) {
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            IOUtils.read(isr, buffer);
            return new String(buffer).trim();
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "ImportAutoBioTemplates ran successfully";
    }

}
