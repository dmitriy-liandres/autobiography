package com.autobiography.resources;

import com.autobiography.db.ProfileDAO;
import com.autobiography.model.db.Person;
import com.autobiography.model.db.Profile;
import com.autobiography.model.view.ProfileViewModel;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.InvocationTargetException;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AjaxDataResource {

    private ProfileDAO profileDAO;

    @Inject
    public AjaxDataResource(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }


    @GET
    @Path("profile")
    @UnitOfWork
    public ProfileViewModel getProfileView(@QueryParam("personId") String personId) throws InvocationTargetException, IllegalAccessException {
        Long personIdLong;
        if (StringUtils.isNotEmpty(personId)) {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, "view", personId));
            personIdLong = Long.valueOf(personId);
        } else {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, "view"));
            Person person = (Person) SecurityUtils.getSubject().getPrincipal();
            personIdLong = person.getId();
        }

        ProfileViewModel profileViewModel = new ProfileViewModel();
        Profile profile = profileDAO.findById(personIdLong);
        if (profile != null) {
            BeanUtils.copyProperties(profileViewModel, profile);
        }
        return profileViewModel;
    }


    @POST
    @Path("profile")
    @UnitOfWork
    public String saveProfileView(@Valid ProfileViewModel profileViewModel) throws InvocationTargetException, IllegalAccessException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.EDIT));
        Person person = (Person) SecurityUtils.getSubject().getPrincipal();
        Profile profile = profileDAO.findById(person.getId());
        if (profile == null) {
            profile = new Profile();
        }
        BeanUtils.copyProperties(profile, profileViewModel);

        profile.setId(person.getId());
        profileDAO.saveOrUpdate(profile);
        SecurityUtils.getSubject().getSession().setAttribute("profile", profile);
        return null;

    }

}
