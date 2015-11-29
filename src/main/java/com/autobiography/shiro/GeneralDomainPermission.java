package com.autobiography.shiro;

import org.apache.shiro.authz.permission.DomainPermission;

import java.util.Set;

/**
 * Author Dmitriy Liandres
 * Date 29.11.2015
 */
public class GeneralDomainPermission extends DomainPermission {
    private PermissionObjectType permissionObjectType;

    public GeneralDomainPermission(PermissionObjectType permissionObjectType, String actions) {
        super(actions);
        setDomain(permissionObjectType.toString());
        this.permissionObjectType = permissionObjectType;
    }

    public GeneralDomainPermission(PermissionObjectType permissionObjectType, String actions, String targets) {
        super(actions, targets);
        setDomain(permissionObjectType.toString());
        this.permissionObjectType = permissionObjectType;
    }

    public GeneralDomainPermission(PermissionObjectType permissionObjectType, Set<String> actions, Set<String> targets) {
        super(actions, targets);
        setDomain(permissionObjectType.toString());
        this.permissionObjectType = permissionObjectType;
    }

    public PermissionObjectType getPermissionObjectType() {
        return permissionObjectType;
    }
}
