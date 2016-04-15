package com.autobiography.helpers;


import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;

import javax.mail.internet.*;

import com.sun.mail.smtp.*;


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
        t.connect("smtp.mailgun.com", "postmaster@sandbox5381a5678d9340c4a1a48bc5ecfdc5d2.mailgun.org", "d6b3305e2cfe11b59362e8f5a33da155");
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }
}
