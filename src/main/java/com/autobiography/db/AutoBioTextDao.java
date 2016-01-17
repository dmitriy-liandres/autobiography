package com.autobiography.db;

import com.autobiography.model.db.AutoBioText;
import com.autobiography.model.db.AutoBioTextType;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
public class AutoBioTextDao extends AbstractDAO<AutoBioText> {
    @Inject
    public AutoBioTextDao(SessionFactory factory) {
        super(factory);
    }

    public AutoBioText findByIdAndType(Long id, AutoBioTextType autoBioTextType) {
        return uniqueResult(namedQuery("AutoBioText.findByIdAndType").setLong("id", id).setString("autoBioTextType", autoBioTextType.name()));

    }

    public AutoBioText create(AutoBioText profile) {
        return persist(profile);
    }

    public void saveOrUpdate(AutoBioText profile) {
        currentSession().saveOrUpdate(profile);
    }

    public void save(AutoBioText profile) {
        currentSession().save(profile);
    }
}
