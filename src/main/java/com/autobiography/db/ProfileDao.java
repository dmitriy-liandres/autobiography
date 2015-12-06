package com.autobiography.db;

import com.autobiography.model.db.Profile;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */
public class ProfileDAO extends AbstractDAO<Profile> {
    @Inject
    public ProfileDAO(SessionFactory factory) {
        super(factory);
    }

    public Profile findById(Long id) {
        return get(id);
    }

    public Profile create(Profile profile) {
        return persist(profile);
    }

    public void saveOrUpdate(Profile profile) {
        currentSession().saveOrUpdate(profile);
    }

    public void save(Profile profile) {
        currentSession().save(profile);
    }
}
