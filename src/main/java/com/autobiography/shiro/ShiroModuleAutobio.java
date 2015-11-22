package com.autobiography.shiro;

import com.autobiography.shiro.realms.AuthenticatingRealmAutobioProvider;
import org.apache.shiro.guice.ShiroModule;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
public class ShiroModuleAutobio extends ShiroModule {


    @Override
    protected void configureShiro() {

        bindRealm().toProvider(AuthenticatingRealmAutobioProvider.class);

    }
}