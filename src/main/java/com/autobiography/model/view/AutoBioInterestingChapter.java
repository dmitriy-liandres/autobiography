package com.autobiography.model.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Author Dmitriy Liandres
 * Date 07.02.2016
 */
public class AutoBioInterestingChapter {
    private Long id;
    private String name;
    private String description;
    private List<AutoBioInterestingSubChapter> subChapters = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AutoBioInterestingSubChapter> getSubChapters() {
        return subChapters;
    }


    public void addSubChapters(AutoBioInterestingSubChapter subChapter) {
        this.subChapters.add(subChapter);
    }

    public void setSubChapters(List<AutoBioInterestingSubChapter> subChapters) {
        this.subChapters = subChapters;
    }
}
