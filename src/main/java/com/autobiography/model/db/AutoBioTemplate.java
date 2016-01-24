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

    @Column(name = "template", nullable = false, unique = false)
    private String template;

    @Column(name = "example", nullable = false, unique = false)
    private String example;

    public String getExample() {
        return example;
    }

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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
