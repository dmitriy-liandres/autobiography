package com.autobiography.model.db;

import javax.persistence.*;

@Entity
@Table(name = "person")
@NamedQueries({
        @NamedQuery(
                name = "com.autobiography.core.Person.findAll",
                query = "SELECT p FROM Person p"
        ),
        @NamedQuery(
                name = "com.autobiography.core.Person.findByUsername",
                query = "SELECT p FROM Person p where username = :username"
        )
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Person() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
