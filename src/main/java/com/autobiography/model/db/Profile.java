package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */
@Entity
@Table(name = "profile")

public class Profile {
    @Id
    private long id;

    @JoinColumn(name = "id")
    @OneToOne(cascade = CascadeType.ALL)
    private Person person;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;

        Profile profile = (Profile) o;

        if (id != profile.id) return false;
        if (person != null ? !person.equals(profile.person) : profile.person != null) return false;
        if (name != null ? !name.equals(profile.name) : profile.name != null) return false;
        return !(surname != null ? !surname.equals(profile.surname) : profile.surname != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }
}
