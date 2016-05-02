package com.autobiography.db;

import com.autobiography.model.db.AutoBioTemplate;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Author Dmitriy Liandres
 * Date 17.01.2016
 */
public class AutoBioTemplateDao extends AbstractDAO<AutoBioTemplate> {
    @Inject
    public AutoBioTemplateDao(SessionFactory factory) {
        super(factory);
    }

    public Optional<AutoBioTemplate> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<AutoBioTemplate> findByLocale(String locale) {
        return list(namedQuery("AutoBioTemplate.findByLocale").setString("locale", locale));
    }

    public int deleteAll() {
        return namedQuery("AutoBioTemplate.deleteAll").executeUpdate();
    }

    public AutoBioTemplate create(AutoBioTemplate autoBioTemplate) {
        return persist(autoBioTemplate);
    }


}
