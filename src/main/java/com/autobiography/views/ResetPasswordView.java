package com.autobiography.views;

/**
 * Author Dmitriy Liandres
 * Date 14.04.2016
 */
public class ResetPasswordView extends GenericView {
    private String errorMessage;

    public ResetPasswordView(String errorMessage) {
        super(RESET_PASSWORD_FTL);
        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
