package com.autobiography.model.view;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */


public class ProfileViewModel {
    private String id;
    @NotEmpty
    @Length(max = 100)
    private String name;
    @NotEmpty
    @Length(max = 100)
    private String surname;

    @NotNull
    private Boolean isPublic;

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
