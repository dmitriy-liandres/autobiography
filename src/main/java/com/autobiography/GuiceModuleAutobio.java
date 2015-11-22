package com.autobiography;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

/**
 * Author Dmitriy Liandres
 * Date 08.10.2015
 */
public class GuiceModuleAutobio extends AbstractModule {

    private HibernateBundle<AutobiographyConfiguration> hibernateBundle;

    public GuiceModuleAutobio(HibernateBundle<AutobiographyConfiguration> hibernateBundle) {
        this.hibernateBundle = hibernateBundle;
    }

    @Override
    protected void configure() {

    }

    @Provides
    public SessionFactory constructSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }
}