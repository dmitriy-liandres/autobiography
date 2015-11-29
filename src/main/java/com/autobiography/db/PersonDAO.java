package com.autobiography.db;

import com.autobiography.core.Person;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class PersonDAO extends AbstractDAO<Person> {
    @Inject
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Person findByUsername(String username) {
        return uniqueResult(namedQuery("com.autobiography.core.Person.findByUsername").setString("username", username));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("com.autobiography.core.Person.findAll"));
    }
}
