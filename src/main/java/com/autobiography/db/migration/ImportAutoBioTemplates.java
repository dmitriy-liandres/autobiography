package com.autobiography.db.migration;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        try {
            dbConnection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();

            //delete old templates
            String sqlDeleteOld =
                    "delete from auto_bio_template";
            psDeleteTemplate = dbConnection.prepareStatement(sqlDeleteOld);
            psDeleteTemplate.executeUpdate();

            //insert new templates
            String sqlInsertTemplate =
                    "insert into auto_bio_template (locale, name, template, example)  values (?, ?, ?, ?) ";
            psInsertTemplate = dbConnection.prepareStatement(sqlInsertTemplate);

            File[] localeDirectories = new File(getClass().getResource("/examples/locales").toURI()).listFiles();
            for (File localeDirectory : localeDirectories) {
                String locale = localeDirectory.getName();
                File[] templates = localeDirectory.listFiles((dir, name) -> {
                    return "biography-for-work".equals(name);
                })[0].listFiles();
                if (templates != null && templates.length > 0) {
                    for (File templateDirectory : templates) {
                       File nameFile = new File(templateDirectory.getPath() + "/name.txt");
                        String name = FileUtils.readFileToString(nameFile);
                        File templateFile = new File(templateDirectory.getPath() + "/template.txt");
                        String template = FileUtils.readFileToString(templateFile);
                        File exampleFile = new File(templateDirectory.getPath() + "/example.txt");
                        String example = FileUtils.readFileToString(exampleFile);
                        psInsertTemplate.setString(1, locale);
                        psInsertTemplate.setString(2, name);
                        psInsertTemplate.setString(3, template);
                        psInsertTemplate.setString(4, example);
                        psInsertTemplate.executeUpdate();
                    }
                }

            }

        } catch (Exception t) {
            throw new CustomChangeException("Migration of ImportAutoBioTemplates failed!", t);
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

    @Override
    public String getConfirmationMessage() {
        return "ImportAutoBioTemplates ran successfully";
    }

}
