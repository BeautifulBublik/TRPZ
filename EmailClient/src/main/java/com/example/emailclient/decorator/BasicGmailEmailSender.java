package com.example.emailclient.decorator;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.emailclient.singelton.ConfigManager;

public class BasicGmailEmailSender implements EmailSender {

	@Override
	public void sendEmail(String fromEmail, String password,
			String toEmail, String subject, String body) {
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
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Лист успішно відправлено!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Помилка надсилання листа: " + e.getMessage());
        }
    }
	}

