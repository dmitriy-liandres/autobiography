package com.autobiography.views;

import com.autobiography.model.db.Profile;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
public class ProfileView extends GenericView {
    private Profile profile;

    public ProfileView(Profile profile) {
        super("profile.ftl");
        this.profile = profile;

    }

    public Profile getProfile() {
        return profile;

    }

}
