package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
@Entity
@Table(name = "autobio_full")
public class AutoBioFull {
    @Id
    private long id;

    @Column(name = "text", nullable = true, unique = false)
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
