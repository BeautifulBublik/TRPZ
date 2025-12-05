package com.example.emailclient.singelton;

import org.springframework.stereotype.Component;

import com.example.emailclient.service.EmailMessageService;
import com.example.emailclient.service.receiver.GmailImapReceiv;
import com.example.emailclient.service.receiver.GmailPop3Receiv;
import com.example.emailclient.service.receiver.IUaImapReceiv;
import com.example.emailclient.service.receiver.IUaPop3Receiv;
import com.example.emailclient.service.receiver.MailReceiver;
import com.example.emailclient.service.receiver.UkrNetImapReceiv;
import com.example.emailclient.service.receiver.UkrNetPop3Receiv;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailReceiverManager {

    private final GmailPop3Receiv gmailPop3Receiv;
    private final GmailImapReceiv gmailImapReceiv;
    private final UkrNetPop3Receiv ukrNetPop3Receiv;
    private final UkrNetImapReceiv ukrNetImapReceiv;
    private final IUaPop3Receiv iUaPop3Receiv;
    private final IUaImapReceiv iUaImapReceiv;

    public MailReceiver getReceiver(String provider, boolean usePop3) {
        return switch (provider.toLowerCase()) {
            case "gmail" -> usePop3 ? gmailPop3Receiv : gmailImapReceiv;
            case "ukr.net" -> usePop3 ? ukrNetPop3Receiv : ukrNetImapReceiv;
            case "i.ua" -> usePop3 ? iUaPop3Receiv : iUaImapReceiv;
            default -> throw new IllegalArgumentException("Невідомий провайдер: " + provider);
        };
    }
}


