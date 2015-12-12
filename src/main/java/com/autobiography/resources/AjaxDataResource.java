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
import org.apache.shiro.SecurityUtils;

import javax.annotation.Nullable;
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
    public ProfileViewModel getProfileView(@Nullable @QueryParam("personId") Long personId) throws InvocationTargetException, IllegalAccessException {
        //todo SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, "view", personId.toString()));
        ProfileViewModel profileViewModel = new ProfileViewModel();
        Profile profile = profileDAO.findById(personId);
        if (profile != null) {
            BeanUtils.copyProperties(profileViewModel, profile);
        }
        return profileViewModel;
    }


    @POST
    @Path("profile")
    @UnitOfWork
    public void saveProfileView(ProfileViewModel profileViewModel) throws InvocationTargetException, IllegalAccessException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.EDIT));
        Person person = (Person) SecurityUtils.getSubject().getPrincipal();
        Profile profile = profileDAO.findById(person.getId());
        if(profile == null){
            profile = new Profile();
        }
        BeanUtils.copyProperties(profile, profileViewModel);

        profile.setId(person.getId());
        profileDAO.saveOrUpdate(profile);

    }

}
