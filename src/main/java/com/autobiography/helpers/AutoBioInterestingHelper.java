package com.autobiography.helpers;

import com.autobiography.model.view.AutoBioInterestingChapter;
import com.autobiography.model.view.AutoBioInterestingSubChapter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Author Dmitriy Liandres
 * Date 07.02.2016
 */
public class AutoBioInterestingHelper {
    //key - locale
    private static final Map<Locale, List<AutoBioInterestingChapter>> CHAPTERS_PER_LOCALE = new HashMap<>();

    //local->chapterId->subChapterId->subChapter
    private static final Map<Locale, Map<Long, Map<Long, AutoBioInterestingSubChapter>>> SUB_CHAPTERS_PER_LOCALE = new HashMap<>();

    public static List<AutoBioInterestingChapter> loadChapters() throws IOException {
        Locale locale = Locale.getDefault();
        List<AutoBioInterestingChapter> autoBioInterestingChapters = CHAPTERS_PER_LOCALE.get(locale);
        if (autoBioInterestingChapters == null) {
            synchronized (CHAPTERS_PER_LOCALE) {
                if (CHAPTERS_PER_LOCALE.get(locale) == null) {
                    SUB_CHAPTERS_PER_LOCALE.putIfAbsent(locale, new HashMap<>());
                    autoBioInterestingChapters = new ArrayList<>();
                    InputStream in = null;
                    try {
                        in = MessageHelper.class.getResourceAsStream("/examples/biography-interesting/chapters.csv");
                        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                        CSVParser parser = new CSVParser(isr, CSVFormat.DEFAULT);
                        AutoBioInterestingChapter autoBioInterestingChapter = null;
                        for (CSVRecord csvRecord : parser.getRecords()) {
                            Long chapterId = Long.valueOf(csvRecord.get(0));
                            String subChapterIdStr = csvRecord.get(1);
                            Long subChapterId = StringUtils.isNoneEmpty(subChapterIdStr) ? Long.valueOf(subChapterIdStr) : null;
                            String nameKey = csvRecord.get(2);
                            if (subChapterId == null) {
                                //new chapter
                                autoBioInterestingChapter = new AutoBioInterestingChapter();
                                autoBioInterestingChapter.setId(chapterId);
                                autoBioInterestingChapter.setName(MessageHelper.message(nameKey));
                                autoBioInterestingChapters.add(autoBioInterestingChapter);
                                SUB_CHAPTERS_PER_LOCALE.get(locale).putIfAbsent(chapterId, new HashMap<>());
                            } else {
                                //new subchapter
                                AutoBioInterestingSubChapter autoBioInterestingSubChapter = new AutoBioInterestingSubChapter();
                                autoBioInterestingSubChapter.setName(MessageHelper.message(nameKey));
                                autoBioInterestingSubChapter.setId(subChapterId);
                                autoBioInterestingChapter.addSubChapters(autoBioInterestingSubChapter);

                                SUB_CHAPTERS_PER_LOCALE.get(locale).get(chapterId).put(subChapterId, autoBioInterestingSubChapter);
                            }
                        }
                        autoBioInterestingChapters.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
                        autoBioInterestingChapters.forEach(chapter -> chapter.getSubChapters().sort((o1, o2) -> o1.getId().compareTo(o2.getId())));
                        CHAPTERS_PER_LOCALE.put(locale, autoBioInterestingChapters);
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }
            }
        }
        return CHAPTERS_PER_LOCALE.get(locale);

    }
}
