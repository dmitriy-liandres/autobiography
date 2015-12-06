package com.autobiography.shiro;

import com.autobiography.shiro.realms.AuthenticatingRealmAutobioProvider;
import com.google.inject.binder.AnnotatedBindingBuilder;
import org.apache.shiro.guice.web.ShiroWebModule;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletContext;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
public class ShiroModuleAutobio extends ShiroWebModule {
    public ShiroModuleAutobio(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    protected void configureShiroWeb() {
        bindRealm().toProvider(AuthenticatingRealmAutobioProvider.class);

    }

    @Override
    protected void bindSessionManager(AnnotatedBindingBuilder<SessionManager> bind) {
        bind.to(DefaultWebSessionManager.class).asEagerSingleton();
    }

}