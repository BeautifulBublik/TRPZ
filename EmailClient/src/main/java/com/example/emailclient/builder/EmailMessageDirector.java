package com.example.emailclient.builder;

import java.util.Date;
import java.util.List;

import com.example.emailclient.model.Attachment;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.model.EmailMessage.EmailStatus;

public class EmailMessageDirector {
    
    private final EmailMessageBuilder builder;

    public EmailMessageDirector(EmailMessageBuilder builder) {
        this.builder = builder;
    }


    public EmailMessage constructSimpleMessage(String from, String subject, String body, Date date, EmailStatus status) {
        return builder
                .setFrom(from)
                .setSubject(subject)
                .setBody(body)
                .setDate(new Date())
                .setStatus(EmailStatus.READ)
                .build();
    }

   
    public EmailMessage constructMessageWithAttachments(String from, String subject, String body, Date date, EmailStatus status, List<Attachment> attachments) {
        return builder
                .setFrom(from)
                .setSubject(subject)
                .setBody(body)
                .setDate(new Date())
                .setStatus(status)
                .addAttachmentAll(attachments)
                .build();
    }
}
