package com.example.emailclient.service.sender;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.example.emailclient.singelton.ConfigManager;

public class GmailSender extends AbstractMailSender implements EmailSender {


    @Override
    public void connect(String from, String password) {

        ConfigManager config = ConfigManager.getInstance();
        String auth = config.getProperty("mail.smtp.auth");
        String starttls = config.getProperty("mail.smtp.starttls.enable");
        String host = config.getProperty("gmail.smtp.host");
        String port = config.getProperty("gmail.smtp.port");

        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        setSession(session);
        setFrom(from);
    }

}

