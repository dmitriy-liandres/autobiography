package com.autobiography.shiro.realms;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
public class AuthenticatingRealmAutobioProvider implements Provider<AuthenticatingRealmAutobio> {
    @Inject
    AuthenticatingRealmAutobio authenticatingRealmAutobio;

    @Override
    public AuthenticatingRealmAutobio get() {
        return authenticatingRealmAutobio;
    }
}