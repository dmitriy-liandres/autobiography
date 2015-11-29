package com.autobiography.shiro;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;

/**
 * Author Dmitriy Liandres
 * Date 29.11.2015
 */
public class AuthorizationInfoExtended implements AuthorizationInfo {
    private Long userId;

    public AuthorizationInfoExtended(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<String> getRoles() {
        return null;
    }

    @Override
    public Collection<String> getStringPermissions() {
        return null;
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return null;
    }
}
