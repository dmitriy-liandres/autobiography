package com.autobiography.model.view;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Author Dmitriy Liandres
 * Date 15.04.2016
 */
public class ContactView {
    @NotEmpty
    @Length(max = 100)
    @Email
    private String email;

    @NotEmpty
    @Length(max = 100)
    private String name;

    @NotEmpty
    @Length(max = 1000)
    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
