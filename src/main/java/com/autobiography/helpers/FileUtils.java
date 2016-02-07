package com.autobiography.helpers;

import com.autobiography.db.AutoBioFileDao;
import com.autobiography.model.db.AutoBioFile;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.google.common.base.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.shiro.SecurityUtils;

import javax.ws.rs.BadRequestException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Author Dmitriy Liandres
 * Date 27.12.2015
 */
public class FileUtils {
    //this will be changed in payment plan
    public static final Integer FILES_PER_PERSON = 5;

    private static final String ROOT_FILES_PATH = "target" + File.separator + "files" + File.separator;

    public static void initFileSystem() throws IOException {
        Path filesDirectory = Paths.get(ROOT_FILES_PATH);
        if (Files.notExists(filesDirectory)) {
            Files.createDirectory(filesDirectory);
        }
    }

    public static Path createDirectoryPath(String directoryId) throws IOException {
        String directoryPath = ROOT_FILES_PATH + directoryId;
        Path filesDirectory = Paths.get(directoryPath);
        if (Files.notExists(filesDirectory)) {
            Files.createDirectory(filesDirectory);
        }
        return filesDirectory;
    }

    /**
     * @param fileItem
     * @param autoBioFile
     * @param autoBioFileDao
     * @return file url
     * @throws IOException
     */
    public static String saveInputStreamToFile(FileItem fileItem, AutoBioFile autoBioFile, AutoBioFileDao autoBioFileDao) throws IOException {
        //1. check that user have files less than maximum
        List<AutoBioFile> allFiles = autoBioFileDao.findByPersonId(autoBioFile.getPerson().getId());
        if (CollectionUtils.isNotEmpty(allFiles) && allFiles.size() >= FILES_PER_PERSON) {
            throw new BadRequestException(String.format(MessageHelper.message("file.tooMany"), FILES_PER_PERSON));

        }
        autoBioFile = autoBioFileDao.create(autoBioFile);
        Path filesDirectory = FileUtils.createDirectoryPath(autoBioFile.getPerson().getId().toString());
        String fileInternalPath = filesDirectory.toString() + File.separator + autoBioFile.getId() + "_" + autoBioFile.getFilename();
        final Path destination = Paths.get(fileInternalPath);
        Files.copy(fileItem.getInputStream(), destination);
        return getFileUrl(autoBioFile);
    }

    public static String getFileUrl(AutoBioFile autoBioFile) throws IOException {
        return "/file/" + autoBioFile.getId() + "/" + autoBioFile.getFilename();
    }


    public static String getFileInternalPathByFileId(Long fileId, AutoBioFileDao autoBioFileDao) throws IOException {
        String fileUrl = null;
        Optional<AutoBioFile> autoBioFileOptional = autoBioFileDao.findById(fileId);
        if (!autoBioFileOptional.isPresent()) {
            fileUrl = "target/classes/assets/img/image_not_available.jpg";
        } else {
            AutoBioFile autoBioFile = autoBioFileOptional.get();
            SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.FILE, PermissionActionType.VIEW, autoBioFile.getPerson().getId().toString()));
            Path filesDirectory = createDirectoryPath(String.valueOf(autoBioFile.getPerson().getId()));
            fileUrl = filesDirectory.toString() + File.separator + fileId + "_" + autoBioFile.getFilename();
        }
        return fileUrl;
    }


}
