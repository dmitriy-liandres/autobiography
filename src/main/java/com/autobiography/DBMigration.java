package com.autobiography;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.CloseableLiquibase;
import liquibase.exception.LiquibaseException;

import java.sql.SQLException;

/**
 * Author Dmitriy Liandres
 * Date 27.11.2015
 */
public class DBMigration {

    public static void update(DatabaseConfiguration<AutobiographyConfiguration> strategy, AutobiographyConfiguration autobiographyConfiguration) throws LiquibaseException, SQLException {
        final PooledDataSourceFactory dbConfig = strategy.getDataSourceFactory(autobiographyConfiguration);
        dbConfig.asSingleConnectionPool();
        CloseableLiquibase closeableLiquibase = new CloseableLiquibase(dbConfig.build(new MetricRegistry(), "liquidbase"));
        closeableLiquibase.update("");
    }
}
