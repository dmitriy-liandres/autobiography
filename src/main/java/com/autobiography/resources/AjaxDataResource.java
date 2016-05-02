package com.autobiography.resources;

import com.autobiography.db.*;
import com.autobiography.helpers.AutoBioInterestingHelper;
import com.autobiography.helpers.EmailHelper;
import com.autobiography.helpers.FileUtils;
import com.autobiography.helpers.MessageHelper;
import com.autobiography.model.db.*;
import com.autobiography.model.view.*;
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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author Dmitriy Liandres
 * Date 06.12.2015
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AjaxDataResource {

    private static final Logger logger = LoggerFactory.getLogger(AjaxDataResource.class);

    private final static Long MAX_FILE_SIZE = 1024 * 1024 * 5L;//5 MB

    private ProfileDAO profileDAO;
    private AutoBioTextDao autoBioTextDao;
    private AutoBioFileDao autoBioFileDao;
    private AutoBioTemplateDao autoBioTemplateDao;
    private AutoBioInterestingAnswersDAO autoBioInterestingAnswersDAO;

    @Inject
    public AjaxDataResource(ProfileDAO profileDAO,
                            AutoBioTextDao autoBioTextDao,
                            AutoBioFileDao autoBioFileDao,
                            AutoBioTemplateDao autoBioTemplateDao,
                            AutoBioInterestingAnswersDAO autoBioInterestingAnswersDAO) {
        this.profileDAO = profileDAO;
        this.autoBioTextDao = autoBioTextDao;
        this.autoBioFileDao = autoBioFileDao;
        this.autoBioTemplateDao = autoBioTemplateDao;
        this.autoBioInterestingAnswersDAO = autoBioInterestingAnswersDAO;
    }


    @GET
    @Path("profile{personId:\\/?[0-9]*}")
    @UnitOfWork
    public ProfileViewModel getProfileView(@PathParam("personId") String personId) throws InvocationTargetException, IllegalAccessException {
        personId = getCorrectPersonId(personId);
        Long personIdLong = checkPersonPermissions(PermissionObjectType.PROFILE, PermissionActionType.VIEW, personId);

        ProfileViewModel profileViewModel = new ProfileViewModel();
        Profile profile = profileDAO.findById(personIdLong);
        if (profile != null) {
            BeanUtils.copyProperties(profileViewModel, profile);
        }
        return profileViewModel;
    }


    @POST
    @Path("profile{personId:\\/?[0-9]*}")
    @UnitOfWork
    public String saveProfileView(@Valid ProfileViewModel profileViewModel) throws InvocationTargetException, IllegalAccessException {
        logger.info("profileViewModel: add, profileViewModel = {}", profileViewModel);
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
    @Path("autobio-text/{autoBioTextType}{personId:\\/?[0-9]*}")
    @UnitOfWork
    public void saveAutobioText(@PathParam("autoBioTextType") AutoBioTextType autoBioTextType,
                                String autobioText) throws InvocationTargetException, IllegalAccessException {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.EDIT));
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
    @Path("autobio-text/{autoBioTextType}{personId:\\/?[0-9]*}")
    @UnitOfWork
    public AutoBioTextView getAutobioText(@PathParam("autoBioTextType") AutoBioTextType autoBioTextType,
                                          @PathParam("personId") String personId) throws InvocationTargetException, IllegalAccessException, IOException {
        personId = getCorrectPersonId(personId);
        Long personIdLong = checkPersonPermissions(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.VIEW, personId);

        switch (autoBioTextType) {
            case FULL:
            case FOR_WORK:
                AutoBioText autoBioText = autoBioTextDao.findByIdAndType(personIdLong, autoBioTextType);
                if (autoBioText != null) {
                    return new AutoBioTextView(autoBioText.getText());
                }
                break;
            case INTERESTING:
                List<AutoBioInterestingChapter> chapters = AutoBioInterestingHelper.loadChapters();
                //chapterId->chapterName
                Map<Long, String> chaptersMap = chapters.stream().collect(Collectors.toMap(AutoBioInterestingChapter::getId, AutoBioInterestingChapter::getName));
                //chapterId->subchapterId->subChapterName
                Map<Long, Map<Long, String>> subChaptersMap = chapters.stream()
                        .collect(Collectors.toMap(AutoBioInterestingChapter::getId,
                                chapter -> chapter.getSubChapters().stream().collect(Collectors.toMap(AutoBioInterestingSubChapter::getId, AutoBioInterestingSubChapter::getName))));


                List<AutoBioInterestingAnswer> autoBioInterestingAnswers = autoBioInterestingAnswersDAO.loadAllPersonAnswers(personIdLong);
                StringBuilder text = new StringBuilder();
                if (CollectionUtils.isNotEmpty(autoBioInterestingAnswers)) {
                    autoBioInterestingAnswers.sort((o1, o2) -> {
                        int chapterComparison = o1.getId().compareTo(o2.getId());
                        if (chapterComparison == 0) {
                            return o1.getSubChapterId().compareTo(o2.getSubChapterId());
                        }
                        return chapterComparison;
                    });
                    Long lastChapter = -1L;
                    for (AutoBioInterestingAnswer answer : autoBioInterestingAnswers) {
                        if (!lastChapter.equals(answer.getChapterId())) {
                            lastChapter = answer.getChapterId();
                            text.append("<div class='chapter-read'>").append(chaptersMap.get(answer.getChapterId())).append("</div><br>");
                        }
                        text.append("<div class='subchapter-read'>").append(subChaptersMap.get(answer.getChapterId()).get(answer.getSubChapterId())).append("</div><br>");
                        text.append(answer.getText());
                    }
                }
                return new AutoBioTextView(text.toString());
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

    /**
     * Checks personPermissions.
     *
     * @param personId person id. Can be null. In this case current user permissions are checked
     * @return person id for which permissoins where checked
     */
    private Long checkPersonPermissions(PermissionObjectType permissionObjectType, String action, String personId) {
        if (StringUtils.isNotEmpty(personId)) {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(permissionObjectType, action, personId));
            return Long.valueOf(personId);
        } else {
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(permissionObjectType, action));
            Person person = (Person) SecurityUtils.getSubject().getPrincipal();
            return person.getId();
        }
    }

    @GET
    @Path("files{personId:\\/?[0-9]*}")
    @UnitOfWork
    public List<UploadedImage> getUploadedFiles(@Context HttpServletRequest request,
                                                @Context ServletContext context,
                                                @QueryParam("CKEditorFuncNum") String CKEditorFuncNum,
                                                @PathParam("personId") String personId) throws FileUploadException, IOException {
        personId = getCorrectPersonId(personId);
        List<UploadedImage> uploadedImages = new ArrayList<>();

        Long personIdLong = checkPersonPermissions(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.VIEW, personId);
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
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.EDIT));
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
            errorMessage = MessageHelper.message("file.notProvided");
        } else if (items.get(0).getSize() > MAX_FILE_SIZE) {
            errorMessage = MessageHelper.message("file.tooBid");
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


    @GET
    @Path("autobio-interesting/chapters")
    @UnitOfWork
    public List<AutoBioInterestingChapter> getAutoBioInterestingChapters() throws InvocationTargetException, IllegalAccessException, IOException {
        return AutoBioInterestingHelper.loadChapters();
    }

    @GET
    @Path("autobio-interesting/answer/chapter/{chapterId}/subChapter/{subChapterId}")
    @UnitOfWork
    public AutoBioInterestingAnswer loadQuestionnaireInterestingAnswer(@PathParam("chapterId") Long chapterId,
                                                                       @PathParam("subChapterId") Long subChapterId,
                                                                       @QueryParam("personId") String personId) throws InvocationTargetException, IllegalAccessException, IOException {
        Long personIdLong = checkPersonPermissions(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.VIEW, personId);
        return autoBioInterestingAnswersDAO.findAnswer(personIdLong, chapterId, subChapterId);
    }

    @POST
    @Path("autobio-interesting/answer/chapter/{chapterId}/subChapter/{subChapterId}/")
    @UnitOfWork
    public void addQuestionnaireInterestingAnswer(@PathParam("chapterId") Long chapterId,
                                                  @PathParam("subChapterId") Long subChapterId,
                                                  @QueryParam("personId") String personId,
                                                  String text) throws InvocationTargetException, IllegalAccessException, IOException {
        Long personIdLong = checkPersonPermissions(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.EDIT, personId);
        AutoBioInterestingAnswer autoBioInterestingAnswer = autoBioInterestingAnswersDAO.findAnswer(personIdLong, chapterId, subChapterId);
        if (autoBioInterestingAnswer == null) {
            autoBioInterestingAnswer = new AutoBioInterestingAnswer();
            autoBioInterestingAnswer.setPerson((Person) SecurityUtils.getSubject().getPrincipal());
            autoBioInterestingAnswer.setChapterId(chapterId);
            autoBioInterestingAnswer.setSubChapterId(subChapterId);
        }
        autoBioInterestingAnswer.setText(text);
        autoBioInterestingAnswersDAO.saveOrUpdate(autoBioInterestingAnswer);

    }


    @GET
    @Path("search/{name}")
    @UnitOfWork
    public List<ProfileViewModel> searchForProfile(@PathParam("name") String name) throws InvocationTargetException, IllegalAccessException, IOException {
        List<Profile> profiles = profileDAO.findByName(name);
        return convertToPublic(profiles);
    }


    @GET
    @Path("all")
    @UnitOfWork
    public List<ProfileViewModel> getAllProfiles() throws InvocationTargetException, IllegalAccessException, IOException {
        List<Profile> profiles = profileDAO.getAllPublic();
        return convertToPublic(profiles);
    }

    @POST
    @Path("contact")
    @UnitOfWork
    public void contact(@Valid ContactView contactView) throws Exception {
        EmailHelper.sendEmail("Message from \"Contact us\" page",
                "name: " + StringEscapeUtils.escapeHtml4(contactView.getName()) + "<br>email: " + StringEscapeUtils.escapeHtml4(contactView.getEmail()) + "<br>message:<br>" + StringEscapeUtils.escapeHtml4(contactView.getMessage()), "dima-amid@tut.by");
    }

    private List<ProfileViewModel> convertToPublic(List<Profile> profiles) {
        List<ProfileViewModel> profileViewModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(profiles)) {
            profiles.stream().forEach(profile -> {
                ProfileViewModel profileViewModel = new ProfileViewModel();
                try {
                    BeanUtils.copyProperties(profileViewModel, profile);
                    profileViewModels.add(profileViewModel);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Impossible to map fields", e);
                }
            });
        }
        return profileViewModels;
    }

    private String getCorrectPersonId(String personId) {
        return StringUtils.isNotEmpty(personId) ? personId.replace("/", "") : null;
    }
}
