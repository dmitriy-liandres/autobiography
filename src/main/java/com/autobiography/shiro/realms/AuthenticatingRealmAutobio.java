package com.autobiography.shiro.realms;

import com.autobiography.db.PersonDAO;
import com.autobiography.model.db.Person;
import com.autobiography.shiro.AuthorizationInfoExtended;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionObjectType;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.DomainPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
public class AuthenticatingRealmAutobio extends AuthorizingRealm {
    private Provider<PersonDAO> personDAOProvider;

    @Inject
    public AuthenticatingRealmAutobio(Provider<PersonDAO> personDAOProvider) {
        this.personDAOProvider = personDAOProvider;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        Person person = personDAOProvider.get().findByUsername(usernamePasswordToken.getUsername());
        if (person == null) {
            throw new UnknownAccountException("No account found for user [" + usernamePasswordToken.getUsername() + "]");
        }

        if (!person.getPassword().equals(new String(usernamePasswordToken.getPassword()))) {
            //todo change message
            throw new UnknownAccountException("Password is not correct for [" + usernamePasswordToken.getUsername() + "]");
        }
        return new SimpleAuthenticationInfo(person, person.getPassword(), getName());
    }


    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Person person = (Person) principals.getPrimaryPrincipal();
        AuthorizationInfoExtended authorizationInfo = new AuthorizationInfoExtended(person.getId());
        return authorizationInfo;
    }


    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        GeneralDomainPermission generalDomainPermission = (GeneralDomainPermission) permission;
        Long userId = ((AuthorizationInfoExtended) info).getUserId();
        Set<DomainPermission> perms = new HashSet<>();
        switch (generalDomainPermission.getPermissionObjectType()) {
            case PROFILE:
                produceProfilePermissions(perms, userId, ((GeneralDomainPermission) permission).getTargets(), ((GeneralDomainPermission) permission).getActions());
                break;
            case FILE:
                produceFilePermissions(perms, userId, ((GeneralDomainPermission) permission).getTargets(), ((GeneralDomainPermission) permission).getActions());
                break;
        }
        if (!perms.isEmpty()) {
            for (Permission perm : perms) {
                if (perm.implies(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void produceFilePermissions(Set<DomainPermission> producedPermissions, Long userId, Set<String> targets, Set<String> actions) {
        //if (org.apache.commons.collections.CollectionUtils.isEmpty(targets)) {
        //user views his own profile
        producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.FILE, actions, targets));

        //}
    }

    private void produceProfilePermissions(Set<DomainPermission> producedPermissions, Long userId, Set<String> targets, Set<String> actions) {
        //if (org.apache.commons.collections.CollectionUtils.isEmpty(targets)) {
        //user views his own profile
        producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.PROFILE, actions, targets));

        //}
    }
}
