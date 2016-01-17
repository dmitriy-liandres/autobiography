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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author Dmitriy Liandres
 * Date 17.01.2016
 */
public class ImportAutoBioTemplates implements CustomTaskChange {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Database database) throws CustomChangeException {
        logger.info("Beginning Default Hiring Rules MigrationHandler");
        Connection dbConnection = null;
        PreparedStatement psInsertTemplate = null;
        try {
            dbConnection = ((JdbcConnection) database.getConnection()).getUnderlyingConnection();
            String sqlInsertTemplate =
                    "insert into auto_bio_template (locale, name, content)  values (?, ?, ?) ";
            psInsertTemplate = dbConnection.prepareStatement(sqlInsertTemplate);

            File[] localeDirectories = new File(getClass().getResource("/examples").toURI()).listFiles();
            for (File localeDirectory : localeDirectories) {
                String locale = localeDirectory.getName();
                File[] templates = localeDirectory.listFiles();
                if (templates != null && templates.length > 0) {
                    for (File templateFile : templates) {
                        String name = templateFile.getName().replace(".txt", "");
                        String content = FileUtils.readFileToString(templateFile);
                        psInsertTemplate.setString(1, locale);
                        psInsertTemplate.setString(2, name);
                        psInsertTemplate.setString(3, content);
                        psInsertTemplate.executeUpdate();
                    }
                }

            }

        } catch (Exception t) {
            throw new CustomChangeException("Migration of Default Hiring Rules failed!", t);
        } finally {
            try {
                if (psInsertTemplate != null) {
                    psInsertTemplate.close();
                }
            } catch (SQLException e) {
                throw new CustomChangeException(e);
            }
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "ImportAutoBioTemplates ran successfully";
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }
}
