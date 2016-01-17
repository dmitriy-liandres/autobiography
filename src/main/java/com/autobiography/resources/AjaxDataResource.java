package com.autobiography.resources;

import com.autobiography.db.AutoBioFileDao;
import com.autobiography.db.AutoBioTemplateDao;
import com.autobiography.db.AutoBioTextDao;
import com.autobiography.db.ProfileDAO;
import com.autobiography.helpers.FileUtils;
import com.autobiography.helpers.MessageProvider;
import com.autobiography.model.db.*;
import com.autobiography.model.view.AutoBioTemplateView;
import com.autobiography.model.view.ProfileViewModel;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AjaxDataResource {

    private final static Long MAX_FILE_SIZE = 1024 * 1024 * 5L;//5 MB

    private ProfileDAO profileDAO;
    private AutoBioTextDao autoBioTextDao;
    private AutoBioFileDao autoBioFileDao;
    private AutoBioTemplateDao autoBioTemplateDao;

    @Inject
    public AjaxDataResource(ProfileDAO profileDAO,
                            AutoBioTextDao autoBioTextDao,
                            AutoBioFileDao autoBioFileDao,
                            AutoBioTemplateDao autoBioTemplateDao) {
        this.profileDAO = profileDAO;
        this.autoBioTextDao = autoBioTextDao;
        this.autoBioFileDao = autoBioFileDao;
        this.autoBioTemplateDao = autoBioTemplateDao;
    }


    @GET
    @Path("profile")
    @UnitOfWork
    public ProfileViewModel getProfileView(@QueryParam("personId") String personId) throws InvocationTargetException, IllegalAccessException {
        Long personIdLong;
        if (StringUtils.isNotEmpty(personId)) {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW, personId));
            personIdLong = Long.valueOf(personId);
        } else {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW));
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

    @POST
    @Path("autobio-text/{autoBioTextType}")
    @UnitOfWork
    public void saveAutobioText(@PathParam("autoBioTextType") AutoBioTextType autoBioTextType,
                                String autobioText) throws InvocationTargetException, IllegalAccessException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.EDIT));
        Person person = (Person) SecurityUtils.getSubject().getPrincipal();
        AutoBioText autoBioText = autoBioTextDao.findByIdAndType(person.getId(), autoBioTextType);
        if (autoBioText == null) {
            autoBioText = new AutoBioText();
            autoBioText.setId(person.getId());
            autoBioText.setAutoBioTextType(autoBioTextType);
        }

        autoBioText.setText(autobioText);
        autoBioTextDao.saveOrUpdate(autoBioText);
    }

    @GET
    @Path("autobio-text/{autoBioTextType}")
    @UnitOfWork
    public AutoBioText getAutobioText(@PathParam("autoBioTextType") AutoBioTextType autoBioTextType,
                                      @QueryParam("personId") String personId) throws InvocationTargetException, IllegalAccessException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.EDIT));
        Person person = (Person) SecurityUtils.getSubject().getPrincipal();
        AutoBioText autoBioText = autoBioTextDao.findByIdAndType(person.getId(), autoBioTextType);
        if (autoBioText != null) {
            return autoBioText;
        }
        return null;
    }

    private class UploadedImage {
        private String image;

        public UploadedImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    @GET
    @Path("files")
    @UnitOfWork
    public List<UploadedImage> getUploadedFiles(@Context HttpServletRequest request,
                                                @Context ServletContext context,
                                                @QueryParam("CKEditorFuncNum") String CKEditorFuncNum,
                                                @QueryParam("personId") String personId) throws FileUploadException, IOException {
        List<UploadedImage> uploadedImages = new ArrayList<>();

        Long personIdLong;
        if (StringUtils.isNotEmpty(personId)) {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW, personId));
            personIdLong = Long.valueOf(personId);
        } else {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW));
            Person person = (Person) SecurityUtils.getSubject().getPrincipal();
            personIdLong = person.getId();
        }
        List<AutoBioFile> autoBioFiles = autoBioFileDao.findByPersonId(personIdLong);
        if (CollectionUtils.isNotEmpty(autoBioFiles)) {
            for (AutoBioFile autoBioFile : autoBioFiles) {
                uploadedImages.add(new UploadedImage(FileUtils.getFileUrl(autoBioFile)));
            }

        }
        return uploadedImages;
    }

    @POST
    @Path("file")
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public String saveFile(@Context HttpServletRequest request,
                           @Context ServletContext context,
                           @QueryParam("CKEditorFuncNum") String CKEditorFuncNum) throws FileUploadException, IOException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.EDIT));
        Person person = (Person) SecurityUtils.getSubject().getPrincipal();
        FileCleaningTracker fileCleaningTracker
                = FileCleanerCleanup.getFileCleaningTracker(context);
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setFileCleaningTracker(fileCleaningTracker);
        ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);

        List<FileItem> items = upload.parseRequest(request);
        String errorMessage = "";
        String fileUrl = "";
        if (CollectionUtils.isEmpty(items)) {
            errorMessage = MessageProvider.message("file.notProvided");
        } else if (items.get(0).getSize() > MAX_FILE_SIZE) {
            errorMessage = MessageProvider.message("file.tooBid");
        } else {
            //save file after validation
            AutoBioFile autoBioFile = new AutoBioFile();
            autoBioFile.setPerson(person);
            autoBioFile.setFileType(FileType.IMAGE);
            autoBioFile.setDataType(DataType.AUTOBIO_FULL);
            autoBioFile.setFilename(items.get(0).getName());
            try {
                fileUrl = FileUtils.saveInputStreamToFile(items.get(0), autoBioFile, autoBioFileDao);
            } catch (BadRequestException e) {
                errorMessage = e.getMessage();
            }
        }

        return
                "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ", '" + fileUrl + "', '" + errorMessage + "');</script>";
    }

    @GET
    @Path("autobio-text/templates/")
    @UnitOfWork
    public List<AutoBioTemplateView> getAutoBioTemplates() throws InvocationTargetException, IllegalAccessException, IOException {
        List<AutoBioTemplate> autoBioTemplates = autoBioTemplateDao.findByLocale(Locale.getDefault().toString());
        List<AutoBioTemplateView> autoBioTemplateViews;
        //let's clear contents because ehy can be too big
        if (CollectionUtils.isNotEmpty(autoBioTemplates)) {
            autoBioTemplateViews = autoBioTemplates.stream().map(autoBioTemplate -> new AutoBioTemplateView(autoBioTemplate.getId(), autoBioTemplate.getName())).collect(Collectors.toList());
        } else {
            autoBioTemplateViews = new ArrayList<>();
        }
        return autoBioTemplateViews;

    }

    @GET
    @Path("autobio-text/templates/{templateId}")
    @UnitOfWork
    public AutoBioTemplate getAutoBioTemplateType(@PathParam("templateId") Long templateId) throws InvocationTargetException, IllegalAccessException, IOException {
        AutoBioTemplate autoBioTemplate = autoBioTemplateDao.findById(templateId).get();
        if (autoBioTemplate == null) {
            autoBioTemplate = new AutoBioTemplate();
        }
        return autoBioTemplate;
    }
}
