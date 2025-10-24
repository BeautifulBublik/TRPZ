package com.example.emailclient.singelton;

import java.util.Properties;

import javax.mail.Session;

public class EmailSessionManager {
	private static EmailSessionManager instance;
	private final Session session;

	private EmailSessionManager() {
		ConfigManager config = ConfigManager.getInstance();

        Properties props = new Properties();
        props.put("mail.store.protocol", config.getProperty("protocol.imap"));
        props.put("mail.imaps.ssl.enable", config.getProperty("mail.imaps.ssl.enable"));
        props.put("mail.imaps.ssl.protocols", config.getProperty("mail.imaps.ssl.protocols"));

        session = Session.getInstance(props);
	}
	public static synchronized EmailSessionManager getInstance() {
		if(instance==null) {
			instance=new EmailSessionManager();
		}
		return instance;
	}
	public Session getSession() {
		return session;
	}
	
	
	
}
