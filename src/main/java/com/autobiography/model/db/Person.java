package com.autobiography.model.db;

import javax.persistence.*;

@Entity
@Table(name = "person")
@NamedQueries({
        @NamedQuery(
                name = "Person.findAll",
                query = "SELECT p FROM Person p"
        ),
        @NamedQuery(
                name = "Person.findByUsername",
                query = "SELECT p FROM Person p where username = :username"
        )
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
