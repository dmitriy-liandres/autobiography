package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
@Entity
@Table(name = "autobio_text")
@NamedQueries({
        @NamedQuery(
                name = "AutoBioText.findByIdAndType",
                query = "SELECT abt FROM AutoBioText abt where abt.id=:id and abt.autoBioTextType=:autoBioTextType"
        )
})

public class AutoBioText {
    @Id
    private long id;

    @Column(name = "text", nullable = true, unique = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "autobio_text_type", nullable = false, unique = false)
    private AutoBioTextType autoBioTextType;

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

    public AutoBioTextType getAutoBioTextType() {
        return autoBioTextType;
    }

    public void setAutoBioTextType(AutoBioTextType autoBioTextType) {
        this.autoBioTextType = autoBioTextType;
    }
}
