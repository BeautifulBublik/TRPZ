package com.example.emailclient.service.providers;

import javax.mail.*;

import com.example.emailclient.singelton.ConfigManager;
import com.example.emailclient.singelton.EmailSessionManager;

import java.util.Properties;

public class IUaValidator implements EmailProviderValidator {

    @Override
    public boolean validate(String email, String password) {
    	ConfigManager config = ConfigManager.getInstance();
    	String protocol = config.getProperty("protocol.imap");
        String host = config.getProperty("iua.imap.host");
        String port = config.getProperty("iua.imap.port");
        String sslEnable= config.getProperty("mail.imaps.ssl.enable");
        String sslProtocols= config.getProperty("mail.imaps.ssl.protocols");
        
        Properties props = new Properties();
        props.put("mail.store.protocol", protocol);
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", port);
        props.put("mail.imaps.ssl.enable", sslEnable);
        props.put("mail.imaps.ssl.protocols", sslProtocols);

        try {
        	Session session= EmailSessionManager.getInstance().getSession();
            Store store = session.getStore(protocol);
            store.connect(host, email, password);
            store.close();
            System.out.println("i.ua акаунт підтверджено");
            return true;
        } catch (AuthenticationFailedException e) {
            System.err.println("i.ua: невірний логін або пароль");
        } catch (Exception e) {
            System.err.println("i.ua помилка: " + e.getMessage());
        }
        return false;
    }
}
