package com.autobiography.resources;

import com.autobiography.db.AutoBioTemplateDao;
import com.autobiography.model.db.AutoBioTemplate;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Author Dmitriy Liandres
 * Date 17.04.2016
 */
@Path("/migration")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class MigrationResource {

    private static final Logger logger = LoggerFactory.getLogger(MigrationResource.class);

    private AutoBioTemplateDao autoBioTemplateDao;

    @Inject
    public MigrationResource(AutoBioTemplateDao autoBioTemplateDao) {
        this.autoBioTemplateDao = autoBioTemplateDao;
    }

    @GET
    @Path("ImportAutoBioTemplates")
    @UnitOfWork
    public String migrateImportAutoBioTemplates() throws InvocationTargetException, IllegalAccessException, IOException {
        logger.info("Start ImportAutoBioTemplates");


        //delete old templates
        autoBioTemplateDao.deleteAll();

        File[] localeDirectories = FileUtils.toFile(getClass().getResource("../../../examples/locales")).listFiles();
        for (File localeDirectory : localeDirectories) {
            String locale = localeDirectory.getName();
            File[] templates = localeDirectory.listFiles((dir, name) -> {
                return "biography-for-work".equals(name);
            })[0].listFiles();
            if (templates != null && templates.length > 0) {
                for (File templateDirectory : templates) {
                    File nameFile = new File(templateDirectory.getPath() + "/name.txt");
                    String name = FileUtils.readFileToString(nameFile);
                    File templateFile = new File(templateDirectory.getPath() + "/template.txt");
                    String template = FileUtils.readFileToString(templateFile);
                    File exampleFile = new File(templateDirectory.getPath() + "/example.txt");
                    String example = FileUtils.readFileToString(exampleFile);
                    AutoBioTemplate autoBioTemplate = new AutoBioTemplate();
                    autoBioTemplate.setName(name);
                    autoBioTemplate.setTemplate(template);
                    autoBioTemplate.setExample(example);
                    autoBioTemplate.setLocale(locale);
                    autoBioTemplateDao.create(autoBioTemplate);
                }
            }


        }
        return "OK";
    }
}
