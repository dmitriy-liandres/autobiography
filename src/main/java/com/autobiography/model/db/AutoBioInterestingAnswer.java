package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 07.02.2016
 */
@Entity
@Table(name = "auto_bio_interesting_answer")
@NamedQueries({
        @NamedQuery(
                name = "AutoBioInterestingAnswer.findAnswer",
                query = "SELECT answer FROM AutoBioInterestingAnswer answer where answer.chapterId=:chapterId and answer.subChapterId=:subChapterId and answer.person.id=:personId"
        )  ,
        @NamedQuery(
                name = "AutoBioInterestingAnswer.loadAllPersonAnswers",
                query = "SELECT answer FROM AutoBioInterestingAnswer answer where answer.person.id=:personId"
        )
})
public class AutoBioInterestingAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Person person;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "sub_chapter_id")
    private Long subChapterId;

    @Column(name = "text")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getSubChapterId() {
        return subChapterId;
    }

    public void setSubChapterId(Long subChapterId) {
        this.subChapterId = subChapterId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
