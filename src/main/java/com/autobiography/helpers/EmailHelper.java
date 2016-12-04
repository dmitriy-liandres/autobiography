package com.autobiography.helpers;


import com.sun.mail.smtp.SMTPTransport;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Date;
import java.util.Properties;


/**
 * Author Dmitriy Liandres
 * Date 14.04.2016
 */
public final class EmailHelper {
    //todo set correct  Recipients
    public static void sendEmail(String subject, String template, String email) throws Exception{
        Properties props = System.getProperties();
        props.put("mail.smtps.host", "smtp.mailgun.org");
        props.put("mail.smtps.auth", "true");
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("dima-amid@tut.by"));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email, false));
        msg.setSubject(subject);
        msg.setContent(template, "text/html; charset=utf-8");
        msg.setSentDate(new Date());
        SMTPTransport t =
                (SMTPTransport) session.getTransport("smtps");
        t.connect("smtp.mailgun.com", "postmaster@historyabout.me", "fbd6f1e1e91c2d20610ac79d8968ee03");
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }

    public static void sendEmail(String subject, String template, String email, String fileName, String attachment) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtps.host", "smtp.mailgun.org");
        props.put("mail.smtps.auth", "true");
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("dima-amid@tut.by"));
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email, false));
        msg.setSubject(subject);
        //    msg.setContent(template, "text/html; charset=utf-8");
        msg.setSentDate(new Date());


        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setText(template);

        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();

        //DataSource source = new FileDataSource(filename);
        DataSource source = new ByteArrayDataSource(attachment.getBytes(), "application/octet-stream");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);

        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        msg.setContent(multipart);


        SMTPTransport t =
                (SMTPTransport) session.getTransport("smtps");
        t.connect("smtp.mailgun.com", "postmaster@historyabout.me", "fbd6f1e1e91c2d20610ac79d8968ee03");
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }
}
