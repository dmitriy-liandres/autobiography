package com.autobiography.views;

import com.autobiography.model.db.AutoBioTextType;

/**
 * Author Dmitriy Liandres
 * Date 27.03.2016
 */
public class AutobiographyReadVew extends GenericView {

    private AutoBioTextType autoBioTextType;

    public AutobiographyReadVew(String templateName,
                                AutoBioTextType autoBioTextType) {
        super(templateName);
        this.autoBioTextType = autoBioTextType;
    }

    public AutoBioTextType getAutoBioTextType() {
        return autoBioTextType;
    }
}
