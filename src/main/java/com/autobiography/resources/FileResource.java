package com.autobiography.resources;

import com.autobiography.db.AutoBioFileDao;
import com.autobiography.helpers.FileUtils;
import com.autobiography.model.db.AutoBioFile;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.shiro.SecurityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

/**
 * Author Dmitriy Liandres
 * Date 10.01.2016
 */
@Path("/file")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class FileResource {

    private AutoBioFileDao autoBioFileDao;

    @Inject
    public FileResource(AutoBioFileDao autoBioFileDao) {
        this.autoBioFileDao = autoBioFileDao;
    }


    @GET
    @Path("/{fileId}/{fileName}")
    @UnitOfWork
    @Produces("image/*")
    public Response getFile(@PathParam("fileId") Long fileId) throws FileUploadException, IOException {
        File fileToSend = new File(FileUtils.getFileInternalPathByFileId(fileId, autoBioFileDao));
        return Response.ok(fileToSend).build();
    }

    @DELETE
    @Path("/{fileId}/{fileName}")
    @UnitOfWork
    public String deleteFile(@PathParam("fileId") Long fileId) throws FileUploadException, IOException {
        Optional<AutoBioFile> autoBioFileOptional = autoBioFileDao.findById(fileId);
        if (!autoBioFileOptional.isPresent()) {
            return null;
        }
        AutoBioFile autoBioFile = autoBioFileOptional.get();
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.AUTOBIOGRAPHY, PermissionActionType.EDIT, autoBioFile.getPerson().getId().toString()));
        File fileToSend = new File(FileUtils.getFileInternalPathByFileId(fileId, autoBioFileDao));
        fileToSend.delete();
        autoBioFileDao.delete(autoBioFile);
        //delete from file system
        return "ok";
    }

}
