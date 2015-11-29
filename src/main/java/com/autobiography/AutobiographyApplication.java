package com.autobiography;

import com.autobiography.core.Person;
import com.autobiography.filters.UserAuthenticationFilter;
import com.autobiography.shiro.ShiroModuleAutobio;
import com.google.inject.Provider;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.guice.aop.ShiroAopModule;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.util.EnumSet;
import java.util.Map;

//import com.yammer.dropwizard.db.DatabaseConfiguration;
//import com.yammer.dropwizard.migrations.ManagedLiquibase;

public class AutobiographyApplication extends Application<AutobiographyConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(AutobiographyApplication.class);

    private GuiceBundle<AutobiographyConfiguration> guiceBundle;



    public static void main(String[] args) throws Exception {
        new AutobiographyApplication().run(args);
    }

    private final HibernateBundle<AutobiographyConfiguration> hibernateBundle =
            new HibernateBundle<AutobiographyConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(AutobiographyConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<AutobiographyConfiguration> bootstrap) {

        bootstrap.addBundle(hibernateBundle);

        guiceBundle = GuiceBundle.<AutobiographyConfiguration>newBuilder()
                .addModule(new GuiceModuleAutobio(hibernateBundle))
                .enableAutoConfig(getClass().getPackage().getName())
                .setConfigClass(AutobiographyConfiguration.class)
                .build();


        bootstrap.addBundle(guiceBundle);


        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new AssetsBundle());

        bootstrap.addBundle(new MigrationsBundle<AutobiographyConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AutobiographyConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new ViewBundle<AutobiographyConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(AutobiographyConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });


    }

    @Override
    public void run(AutobiographyConfiguration configuration, Environment environment) throws Exception {

        org.apache.shiro.mgt.SecurityManager securityManager = guiceBundle.getInjector().getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

//
//        final Template template = configuration.buildTemplate();
//
//        environment.healthChecks().register("template", new TemplateHealthCheck(template));
//
//        environment.jersey().register(RolesAllowedDynamicFeature.class);
//
//
//        environment.servlets().addFilter("UserAuthenticationFilter", new UserAuthenticationFilter())
//                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        migrateDB(configuration);


    }


    public void migrateDB(AutobiographyConfiguration configuration) throws Exception {
        DBMigration.update(hibernateBundle, configuration);

    }


}
