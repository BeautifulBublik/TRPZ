package com.example.emailclient.service.sender;


import java.util.Properties;

import javax.mail.Authenticator;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;


import com.example.emailclient.singelton.ConfigManager;

public class UkrNetSender extends AbstractMailSender implements EmailSender {

	    @Override
	    public void connect(String from, String password) {

	        ConfigManager config = ConfigManager.getInstance();
	        String auth = config.getProperty("mail.smtp.auth");
	        String starttls = config.getProperty("mail.smtp.starttls.enable");
	        String host = config.getProperty("ukrnet.smtp.host");
	        String port = config.getProperty("ukrnet.smtp.port");

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
