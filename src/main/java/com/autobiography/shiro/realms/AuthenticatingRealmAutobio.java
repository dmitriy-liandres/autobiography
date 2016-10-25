package com.autobiography.shiro.realms;

import com.autobiography.db.PersonDAO;
import com.autobiography.db.ProfileDAO;
import com.autobiography.model.db.Person;
import com.autobiography.model.db.Profile;
import com.autobiography.shiro.AuthorizationInfoExtended;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.DomainPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
public class AuthenticatingRealmAutobio extends AuthorizingRealm {
    private Provider<PersonDAO> personDAOProvider;
    private Provider<ProfileDAO> profileDAOProvider;

    @Inject
    public AuthenticatingRealmAutobio(Provider<PersonDAO> personDAOProvider,
                                      Provider<ProfileDAO> profileDAOProvider) {
        this.personDAOProvider = personDAOProvider;
        this.profileDAOProvider = profileDAOProvider;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        if (usernamePasswordToken.getUsername() != null) {
            Person person = personDAOProvider.get().findByUsername(usernamePasswordToken.getUsername());
            if (person == null) {
                throw new UnknownAccountException("No account found for user [" + usernamePasswordToken.getUsername() + "]");
            }

            if (!person.getPassword().equals(new String(usernamePasswordToken.getPassword()))) {
                throw new UnknownAccountException("Password is not correct for [" + usernamePasswordToken.getUsername() + "]");
            }
            return new SimpleAuthenticationInfo(person, person.getPassword(), getName());
        } else {
            return new SimpleAuthenticationInfo(new Person(), "", getName());
        }

    }


    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Person person = (Person) principals.getPrimaryPrincipal();
        return new AuthorizationInfoExtended(person.getId());
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
            case AUTOBIOGRAPHY:
                produceAutobiographyPermissions(perms, userId, ((GeneralDomainPermission) permission).getTargets(), ((GeneralDomainPermission) permission).getActions());
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

    private void produceAutobiographyPermissions(Set<DomainPermission> producedPermissions, Long currentUserId, Set<String> targets, Set<String> actions) {
        if (CollectionUtils.isEmpty(targets)) {
            //user views his own profile
            producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.AUTOBIOGRAPHY, actions, targets));
        } else {
            Set<String> profilesAllowedToView = new HashSet<>();
            targets.stream().filter(target -> target != null).forEach(target -> {
                if (currentUserId != null && target.equals(currentUserId.toString())) {
                    //user views his own profile
                    profilesAllowedToView.add(target);
                } else {
                    Profile profile = profileDAOProvider.get().findById(Long.valueOf(target));
                    if (profile != null && profile.getIsPublic()) {
                        profilesAllowedToView.add(target);
                    }
                }
            });
            if (!profilesAllowedToView.isEmpty()) {
                producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.AUTOBIOGRAPHY, Collections.singleton(PermissionActionType.VIEW), profilesAllowedToView));
            }
        }
    }

    private void produceProfilePermissions(Set<DomainPermission> producedPermissions, Long currentUserId, Set<String> targets, Set<String> actions) {
        if (CollectionUtils.isEmpty(targets)) {
            //user views his own profile
            producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.PROFILE, actions, targets));
        } else {
            Set<String> profilesAllowedToView = new HashSet<>();
            targets.stream().filter(target -> target != null).forEach(profilesAllowedToView::add);
            if (!profilesAllowedToView.isEmpty()) {
                producedPermissions.add(new GeneralDomainPermission(PermissionObjectType.PROFILE, Collections.singleton(PermissionActionType.VIEW), profilesAllowedToView));
            }
        }
    }
}
