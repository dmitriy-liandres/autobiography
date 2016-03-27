package com.autobiography.model.db;

import javax.persistence.*;
import java.io.Serializable;

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

@IdClass( AutoBioText.PK.class )
public class AutoBioText {
    @Id
    private Long id;

    @Column(name = "text", nullable = true, unique = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "autobio_text_type", nullable = false, unique = false)
    @Id
    private AutoBioTextType autoBioTextType;


    public static class PK implements Serializable {
        private long id;
        private AutoBioTextType autoBioTextType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
