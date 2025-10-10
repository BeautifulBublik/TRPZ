package com.example.emailclient.service.providers;

import javax.mail.*;
import java.util.Properties;

public class UkrNetValidator implements EmailProviderValidator {

    @Override
    public boolean validate(String email, String password) {
        String host = "imap.ukr.net";
        Properties props = new Properties();
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", "993");
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, email, password);
            store.close();
            System.out.println("Ukr.net акаунт підтверджено");
            return true;
        } catch (AuthenticationFailedException e) {
            System.err.println("Ukr.net: невірний логін або пароль");
        } catch (Exception e) {
            System.err.println("Ukr.net помилка: " + e.getMessage());
        }
        return false;
    }
}
