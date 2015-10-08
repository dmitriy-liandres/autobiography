package com.autobiography;
import com.autobiography.auth.ExampleAuthorizer;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.auth.AuthValueFactoryProvider;
import com.autobiography.auth.ExampleAuthenticator;
import com.autobiography.cli.RenderCommand;
import com.autobiography.core.Person;
import com.autobiography.core.Template;
import com.autobiography.core.User;
import com.autobiography.db.PersonDAO;
import com.autobiography.filter.DateRequiredFeature;
import com.autobiography.health.TemplateHealthCheck;
import com.autobiography.resources.FilteredResource;
import com.autobiography.resources.HelloWorldResource;
import com.autobiography.resources.PeopleResource;
import com.autobiography.resources.PersonResource;
import com.autobiography.resources.ProtectedResource;
import com.autobiography.resources.ViewResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import java.util.Map;

public class AutobiographyApplication extends Application<AutobiographyConfiguration> {
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
        GuiceBundle<AutobiographyConfiguration> guiceBundle = GuiceBundle.<AutobiographyConfiguration>newBuilder()
                .addModule(new GuiceModule(hibernateBundle))
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

        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<AutobiographyConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AutobiographyConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle<AutobiographyConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(AutobiographyConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @Override
    public void run(AutobiographyConfiguration configuration, Environment environment) {
        System.out.println(1);

   /*
     Injector injector = Guice.createInjector();
       environment.jersey().register(injector.getInstance(ViewResource.class));


        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        final Template template = configuration.buildTemplate();

        environment.healthChecks().register("template", new TemplateHealthCheck(template));
        environment.jersey().register(DateRequiredFeature.class);
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new ExampleAuthenticator())
                .setAuthorizer(new ExampleAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new HelloWorldResource(template));
        //environment.jersey().register(new ViewResource());
        environment.jersey().register(new ProtectedResource());
        environment.jersey().register(new PeopleResource(dao));
        environment.jersey().register(new PersonResource(dao));
        environment.jersey().register(new FilteredResource());    */
    }


}
