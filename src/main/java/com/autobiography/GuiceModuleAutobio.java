package com.autobiography;

import com.autobiography.db.PersonDAO;
import com.autobiography.db.ProfileDAO;
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
    @Singleton
    public SessionFactory constructSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    public PersonDAO providesPersonDAO() {
        return new PersonDAO(constructSessionFactory());
    }

    @Provides
    @Singleton
    public ProfileDAO providesProfileDAO() {
        return new ProfileDAO(constructSessionFactory());
    }


}