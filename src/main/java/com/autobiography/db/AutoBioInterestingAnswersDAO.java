package com.autobiography.db;

import com.autobiography.model.db.AutoBioInterestingAnswer;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Author Dmitriy Liandres
 * Date 07.02.2016
 */
public class AutoBioInterestingAnswersDAO extends AbstractDAO<AutoBioInterestingAnswer> {
    @Inject
    public AutoBioInterestingAnswersDAO(SessionFactory factory) {
        super(factory);
    }

    public AutoBioInterestingAnswer create(AutoBioInterestingAnswer autoBioInterestingAnswer) {
        return persist(autoBioInterestingAnswer);
    }

    public void saveOrUpdate(AutoBioInterestingAnswer autoBioInterestingAnswer) {
        currentSession().saveOrUpdate(autoBioInterestingAnswer);
    }

    public void save(AutoBioInterestingAnswer autoBioInterestingAnswer) {
        currentSession().save(autoBioInterestingAnswer);
    }

    public AutoBioInterestingAnswer findAnswer(Long personId, Long chapterId, Long subChapterId) {
        return uniqueResult(namedQuery("AutoBioInterestingAnswer.findAnswer")
                .setLong("personId", personId)
                .setLong("chapterId", chapterId)
                .setLong("subChapterId", subChapterId));

    }
}
