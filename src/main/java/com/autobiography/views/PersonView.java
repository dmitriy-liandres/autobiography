package com.autobiography.views;

import com.autobiography.core.Person;

import io.dropwizard.views.View;

public class PersonView extends GenericView {
    private final Person person;


    public PersonView(Person person) {
        super("person.ftl");
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
