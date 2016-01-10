package com.autobiography.db;

import com.autobiography.model.db.AutoBioFull;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
public class AutoBioFullDao extends AbstractDAO<AutoBioFull> {
    @Inject
    public AutoBioFullDao(SessionFactory factory) {
        super(factory);
    }

    public AutoBioFull findById(Long id) {
        return get(id);
    }

    public AutoBioFull create(AutoBioFull profile) {
        return persist(profile);
    }

    public void saveOrUpdate(AutoBioFull profile) {
        currentSession().saveOrUpdate(profile);
    }

    public void save(AutoBioFull profile) {
        currentSession().save(profile);
    }
}
