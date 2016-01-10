package com.autobiography.db;

import com.autobiography.model.db.AutoBioFile;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Author Dmitriy Liandres
 * Date 10.01.2016
 */
public class AutoBioFileDao extends AbstractDAO<AutoBioFile> {
    @Inject
    public AutoBioFileDao(SessionFactory factory) {
        super(factory);
    }

    public Optional<AutoBioFile> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<AutoBioFile> findByPersonId(Long personId) {
        return list(namedQuery("AutoBioFile.findByPersonId").setLong("personId", personId));
    }

    public AutoBioFile create(AutoBioFile autoBioFile) {
        return persist(autoBioFile);
    }

    public void delete(AutoBioFile autoBioFile) {
        currentSession().delete(autoBioFile);
    }

}
