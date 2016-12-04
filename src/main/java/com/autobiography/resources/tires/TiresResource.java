package com.autobiography.resources.tires;

import com.autobiography.helpers.EmailHelper;
import com.autobiography.resources.tires.model.TiresJsonItem;
import com.autobiography.resources.tires.model.TiresJsonResponse;
import com.autobiography.resources.tires.model.TiresResultItem;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * This resource is used parsing http://tires.yad2.co.il/
 * Thus it is not related to autobio itself
 * Author Dmitriy Liandres
 * Date 25.11.2016
 */
@Path("/tires/")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class TiresResource {
    private static final Random random = new Random();

    private static final ZoneId zone = ZoneId.of("Asia/Jerusalem");

    private static final Logger logger = LoggerFactory.getLogger(TiresResource.class);

    private static final Integer maxHourToSendRequests = 20;
    private static final Integer minHourToSendRequests = 8;

    @GET
    @Produces("application/csv")
    public byte[] parseRires(@Context HttpServletResponse response,
                             @QueryParam("email") String email,
                             @QueryParam("diameter") List<String> providedDiameters,
                             @QueryParam("aspectRatio") List<String> providedAspectRatios,
                             @QueryParam("nominalWidth") List<String> providedNominalWidths) throws Exception {

        Date start = new Date();
        logger.info("parseRires start at {}", start);

        ObjectMapper objectMapper = new ObjectMapper();


        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        StringBuilder result = new StringBuilder();
        //add header

        result.append("Rim diameter code;Ratio of height to width (aspect ratio);Nominal width of tire in millimeters;Producer;Tire description;Tire type;Speed symbol;Country of Origin;Price;Town;Telephone number\n");
        List<TiresResultItem> tiresResultItems = new ArrayList<>();

        //load diameters
        List<TiresJsonItem> diameters = new ArrayList<>();
        Document mainPage = Jsoup.parse(loadAnyPageData("http://oruxezlt.pfqwimq.mnxs42lm.cmle.ru/"));
        Element selectWithDiameters = mainPage.getElementById("diameterID");
        Elements options = selectWithDiameters.getElementsByTag("option");
        for (Element option : options) {
            String value = option.attr("value");
            String text = option.text();
            if ("-1".equals(value)) {
                continue;
            }
            if (CollectionUtils.isEmpty(providedDiameters) || providedDiameters.contains(text)) {
                diameters.add(new TiresJsonItem(value, text));
            }
        }


        for (TiresJsonItem diameter : diameters) {
            logger.info("diameter = {}", diameter);
            overworkLoadDiameter(tiresResultItems, 1, objectMapper, diameter, providedAspectRatios, providedNominalWidths);
        }


        response.setContentType("text/csv");
        response.setHeader("Content-Transfer-Encoding", "Binary");
        response.setHeader("Content-Disposition", "attachment;filename=tires.csv");

        tiresResultItems.forEach(item -> result.append(item.toString()));

        String resultStr = result.toString();
        Date end = new Date();
        EmailHelper.sendEmail("Tire Data", "Tire Data is in the attachment", email, "tires.csv", resultStr);

        logger.info("parseRires end at {}, duration = {}", end, (end.getTime() - start.getTime()) / 1000);
        logger.info("parseRires result = {}", resultStr);

        return resultStr.getBytes();
    }

    private void overworkLoadDiameter(List<TiresResultItem> tiresResultItems, int retryNumber, ObjectMapper objectMapper,
                                      TiresJsonItem diameter, List<String> providedAspectRatios, List<String> providedNominalWidths) {
        try {

            String aspectRatioStr = loadAnyJsonData("http://oruxezlt.pfqwimq.mnxs42lm.cmle.ru/index.php?action=getAspectRatioList", "diameter=" + diameter.getVal() + "&aspectRatio=-1&type=-1&area=-1");
            TiresJsonResponse aspectRatioJson = objectMapper.readValue(aspectRatioStr, TiresJsonResponse.class);
            if (aspectRatioJson.getResult() == null) {
                return;
            }
            for (TiresJsonItem aspectRatio : aspectRatioJson.getResult()) {
                if (CollectionUtils.isEmpty(providedAspectRatios) || providedAspectRatios.contains(aspectRatio.getText())) {
                    overworkLoadAspectRatio(tiresResultItems, 1, objectMapper, diameter, aspectRatio, providedNominalWidths);
                }
            }
        } catch (Exception e) {
            logger.error("Impossible to overwork diameter = {}", diameter, e);
            if (retryNumber < 3) {
                overworkLoadDiameter(tiresResultItems, retryNumber + 1, objectMapper, diameter, providedAspectRatios, providedNominalWidths);
            } else {
                throw new RuntimeException("Impossible to overworkPrices");
            }
        }
    }

    private void overworkLoadAspectRatio(List<TiresResultItem> tiresResultItems, int retryNumber, ObjectMapper objectMapper,
                                         TiresJsonItem diameter, TiresJsonItem aspectRatio, List<String> providedNominalWidths) {

        try {
            logger.info("diameter = {}; aspectRatio = {}", diameter, aspectRatio);
            String nominalWidthOfTireInMillimetersStr = null;
            nominalWidthOfTireInMillimetersStr = loadAnyJsonData("http://oruxezlt.pfqwimq.mnxs42lm.cmle.ru/index.php?action=getWidthList", "diameter=" + diameter.getVal() + "&aspectRatio=" + aspectRatio.getVal() + "&type=-1&area=-1");
            TiresJsonResponse nominalWidthOfTireInMillimetersStrJson = objectMapper.readValue(nominalWidthOfTireInMillimetersStr, TiresJsonResponse.class);
            if (nominalWidthOfTireInMillimetersStrJson.getResult() == null) {
                return;
            }
            for (TiresJsonItem nominalWidth : nominalWidthOfTireInMillimetersStrJson.getResult()) {
                if (CollectionUtils.isEmpty(providedNominalWidths) || providedNominalWidths.contains(nominalWidth.getText())) {
                    overworkResultPage(tiresResultItems, 1, diameter, aspectRatio, nominalWidth);
                }
            }
        } catch (Exception e) {
            logger.error("Impossible to overwork diameter = {}; aspectRatio = {}", diameter, aspectRatio, e);
            if (retryNumber < 3) {
                overworkLoadAspectRatio(tiresResultItems, retryNumber + 1, objectMapper, diameter, aspectRatio, providedNominalWidths);
            } else {
                throw new RuntimeException("Impossible to overworkPrices");
            }
        }
    }

    private void overworkResultPage(List<TiresResultItem> tiresResultItems, int retryNumber,
                                    TiresJsonItem diameter, TiresJsonItem aspectRatio, TiresJsonItem nominalWidth) {
        try {
            logger.info("diameter = {}; aspectRatio = {}; nominalWidth = {}", diameter, aspectRatio, nominalWidth);
            Document page = Jsoup.parse(loadAnyPageData("http://oruxezlt.pfqwimq.mnxs42lm.cmle.ru/compare?diameter=" + diameter.getVal() + "&aspectRatio=" + aspectRatio.getVal() + "&width=" + nominalWidth.getVal()));
            Elements resultDataTable = page.getElementsByClass("resultDataTable");
            if (resultDataTable.size() == 0) {
                return;
            }
            Elements resultDataTableRows = resultDataTable.get(0).getElementsByTag("tr");
            if (resultDataTableRows.size() == 0) {
                return;
            }
            for (Element row : resultDataTableRows) {
                Elements tireData = row.getElementsByClass("tireData");
                if (tireData.size() == 0) {
                    continue;
                }
                Element dataRow = tireData.get(0).getElementsByClass("dataRow").get(0);
                Elements itemData = dataRow.getElementsByClass("item-Data");
                String producer = itemData.get(0).getElementsByTag("div").get(1).text();
                String description = itemData.get(1).getElementsByTag("div").get(1).text();
                String type = itemData.get(2).getElementsByTag("div").get(1).text();
                String speedSymbol = itemData.get(3).getElementsByTag("div").get(1).text();
                String countryOfOrigin = itemData.size() >= 5 ? itemData.get(4).getElementsByTag("div").get(1).text() : "";

                String urlToPrices = row.getElementsByClass("prices").get(0).getElementsByTag("a").get(0).attr("href");

                overworkPrices(tiresResultItems, 1, diameter, aspectRatio, nominalWidth, urlToPrices, producer, description, type, speedSymbol, countryOfOrigin);

            }
        } catch (Exception e) {
            logger.error("Impossible to overwork diameter = {}; aspectRatio = {}; nominalWidth = {}", diameter, aspectRatio, nominalWidth, e);
            if (retryNumber < 3) {
                overworkResultPage(tiresResultItems, retryNumber + 1, diameter, aspectRatio, nominalWidth);
            } else {
                throw new RuntimeException("Impossible to overworkPrices");
            }
        }
    }

    private void overworkPrices(List<TiresResultItem> tiresResultItems, int retryNumber,
                                TiresJsonItem diameter, TiresJsonItem aspectRatio, TiresJsonItem nominalWidth, String urlToPrices,
                                String producer, String description, String type, String speedSymbol, String countryOfOrigin) throws IOException, InterruptedException {
        try {
            logger.info("diameter = {}; aspectRatio = {}; nominalWidth = {}, urlToPrices = {}", diameter, aspectRatio, nominalWidth, urlToPrices);
            Document pricesPage = Jsoup.parse(loadAnyPageData(urlToPrices));
            Elements resultDataPrices = pricesPage.getElementsByClass("resultData").get(0).getElementsByClass("publishTireshop");
            for (Element priceLine : resultDataPrices) {

                String price = priceLine.getElementsByClass("PriceCenter").get(0).text();
                String town = priceLine.getElementsByClass("publishRight").get(0).getElementsByTag("tr").get(2).getElementsByTag("label").get(1).text();
                String phoneNumber = priceLine.getElementsByClass("leftTarget").get(0).getElementsByClass("phoneNumber").get(0).text();
                //result.append(diameter.getText()).append(";").append(aspectRatio.getText()).append(";").append(nominalWidth.getText()).append(";").append(producer).append(";").append(description).append(";").append(type).append(";").append(speedSymbol).append(";").append(countryOfOrigin).append(";").append(price).append(";").append(town).append(";").append(phoneNumber).append("\n");
                TiresResultItem tiresResultItem = new TiresResultItem();
                tiresResultItem.setDiameter(diameter);
                tiresResultItem.setAspectRatio(aspectRatio);
                tiresResultItem.setNominalWidth(nominalWidth);
                tiresResultItem.setProducer(producer);
                tiresResultItem.setDescription(description);
                tiresResultItem.setType(type);
                tiresResultItem.setSpeedSymbol(speedSymbol);
                tiresResultItem.setCountryOfOrigin(countryOfOrigin);
                tiresResultItem.setPrice(price);
                tiresResultItem.setTown(town);
                tiresResultItem.setPhoneNumber(phoneNumber);
                tiresResultItems.add(tiresResultItem);
            }
        } catch (Exception e) {
            logger.error("diameter = {}; aspectRatio = {}; nominalWidth = {}, urlToPrices = {}, retryNumber = {}", diameter, aspectRatio, nominalWidth, urlToPrices, retryNumber, e);
            if (retryNumber < 3) {
                overworkPrices(tiresResultItems, retryNumber + 1, diameter, aspectRatio, nominalWidth, urlToPrices, producer, description, type, speedSymbol, countryOfOrigin);
            } else {
                throw new RuntimeException("Impossible to overworkPrices");
            }
        }
    }


    private String loadAnyJsonData(String url, String data) throws IOException, InterruptedException {
        timeBlocker();
        //sleep 0.5-1.5 sec
        Thread.sleep(3000 + random.nextInt(6000));  //should be around 6 sec (3-9)
        URL urlAll = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlAll.openConnection();
        //add request header. Without this header we will get full page, not just required part
        con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        //con.setRequestProperty("Accept-Encoding:gzip, deflate");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,uk;q=0.4");
        con.setRequestProperty("Connection", "keep-alive");
        //con.setRequestProperty("Content-Length:41");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestProperty("Cookie", "__gads=ID=9f9fe185f495d383:T=1479826231:S=ALNI_MbNhN16liIRGRrc8h6UD-tTBjMdLQ; favorites_Tires_userid=ffg1122542713; _ym_uid=1479833618107190641; _ga=GA1.2.874527927.1479833696; yad2upload=1711276042.20480.0000; _dc_gtm_UA-708051-1=1; _ga=GA1.5.1731918801.1479826229");
        con.setRequestProperty("Host", "http://tires.yad2.co.il");
        con.setRequestProperty("Origin", "http://tires.yad2.co.il");
        con.setRequestProperty("Referer", "http://tires.yad2.co.il/");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

        con.setReadTimeout(30000);
        con.setConnectTimeout(30000);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);


        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();


        String response = IOUtils.toString(con.getInputStream(), "UTF-8");
        return response;
    }

    private String loadAnyPageData(String url) throws IOException, InterruptedException {
        timeBlocker();
        //sleep 0.5-1.5 sec
        Thread.sleep(3000 + random.nextInt(6000));  //should be around 6 sec (3-9)
        URL urlAll = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlAll.openConnection();
        //add request header. Without this header we will get full page, not just required part
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        //con.setRequestProperty("Accept-Encoding:gzip, deflate, sdch");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,uk;q=0.4");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Cookie", "__gads=ID=9f9fe185f495d383:T=1479826231:S=ALNI_MbNhN16liIRGRrc8h6UD-tTBjMdLQ; favorites_Tires_userid=ffg1122542713; _ym_uid=1479833618107190641; _ga=GA1.2.874527927.1479833696; yad2upload=1711276042.20480.0000; _ga=GA1.5.1731918801.1479826229; _dc_gtm_UA-708051-1=1");
        con.setRequestProperty("Host", "http://tires.yad2.co.il");
        con.setRequestProperty("Referer", "http://tires.yad2.co.il/");
        con.setRequestProperty("Upgrade-Insecure-Requests", "1");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");


        String response = IOUtils.toString(con.getInputStream(), "UTF-8");
        return response;
    }

    private void timeBlocker() throws InterruptedException {

        LocalDateTime localDateTime = LocalDateTime.now(zone);

        ZonedDateTime zdt = localDateTime.atZone(zone);
        ZoneOffset offset = zdt.getOffset();

        //we start at 8 AM and end at 8 PM
        LocalDateTime startTime = null;
        if (localDateTime.getHour() >= maxHourToSendRequests) {
            //next date 8 am
            startTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth() + 1, minHourToSendRequests, 0);
        }
        if (localDateTime.getHour() < minHourToSendRequests) {
            //the same date 8 am
            startTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), minHourToSendRequests, 0);
        }
        if (startTime != null) {
            long timeToStartWork = startTime.toInstant(offset).toEpochMilli();
            long now = localDateTime.toInstant(offset).toEpochMilli();
            long sleepInMs = timeToStartWork - now;
            logger.info("Sleep {} ms = {} sec = {} min = {} hours", sleepInMs, sleepInMs / 1000, sleepInMs / 1000 / 60, sleepInMs / 1000 / 60 / 60);
            Thread.sleep(timeToStartWork - now);
        }
    }


}

