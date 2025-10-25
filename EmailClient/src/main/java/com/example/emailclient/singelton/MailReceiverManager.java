package com.example.emailclient.singelton;

import com.example.emailclient.service.receiver.GmailImapReceiv;
import com.example.emailclient.service.receiver.GmailPop3Receiv;
import com.example.emailclient.service.receiver.IUaImapReceiv;
import com.example.emailclient.service.receiver.IUaPop3Receiv;
import com.example.emailclient.service.receiver.MailReceiverImap;
import com.example.emailclient.service.receiver.UkrNetImapReceiv;
import com.example.emailclient.service.receiver.UkrNetPop3Receiv;

public class MailReceiverManager {
	private static MailReceiverManager instance;

    private MailReceiverManager() {}

    public static synchronized MailReceiverManager getInstance() {
        if (instance == null) instance = new MailReceiverManager();
        return instance;
    }

    public MailReceiverImap getReceiver(String provider, boolean usePop3) {
        return switch (provider.toLowerCase()) {
            case "gmail" -> usePop3 ? new GmailPop3Receiv() : new GmailImapReceiv();
            case "ukr.net" -> usePop3 ? new UkrNetPop3Receiv() : new UkrNetImapReceiv();
            case "i.ua" -> usePop3 ? new IUaPop3Receiv() : new IUaImapReceiv();
            default -> throw new IllegalArgumentException("Невідомий провайдер: " + provider);
        };
    }
}

