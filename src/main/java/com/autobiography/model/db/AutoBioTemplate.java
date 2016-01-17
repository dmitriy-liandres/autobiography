package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 17.01.2016
 */
@Entity
@Table(name = "auto_bio_template")
@NamedQueries({
        @NamedQuery(
                name = "AutoBioTemplate.findByLocale",
                query = "SELECT abt FROM AutoBioTemplate abt where abt.locale=:locale"
        )
})
public class AutoBioTemplate {
    @Id
    private long id;

    @Column(name = "locale", nullable = false, unique = false)
    private String locale;

    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @Column(name = "content", nullable = false, unique = false)
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
