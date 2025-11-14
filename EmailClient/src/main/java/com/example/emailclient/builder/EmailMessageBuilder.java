package com.example.emailclient.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.Attachment;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.model.EmailMessage.EmailStatus;

public class EmailMessageBuilder implements Builder<EmailMessage> {
	private String from;
    private String subject;
    private String body;
    private Date date;
    private EmailStatus status;
    private List<Attachment> attachments = new ArrayList<>();
    private Account account;
    

	public  EmailMessageBuilder setAccount(Account account) {
		this.account = account;
		return this;
	}
	public EmailMessageBuilder setFrom(String from) {
    	this.from=from;
    	return this;
    }
    public EmailMessageBuilder setSubject(String subject) {
    	this.subject=subject;
    	return this;
    }
    public EmailMessageBuilder setBody(String body) {
    	this.body=body;
    	return this;
    }
    public EmailMessageBuilder setDate(Date date) {
    	this.date=date;
    	return this;
    }
    public EmailMessageBuilder setStatus(EmailStatus status) {
    	this.status=status;
    	return this;
    }
    public EmailMessageBuilder addAttachmentAll(List<Attachment> attachments) {
    	this.attachments=attachments;
    	return this;
    }
    public EmailMessageBuilder addAttachment(Attachment attachment) {
    	attachments.add(attachment);
    	return this;
    }
    
	@Override
	public EmailMessage build() {
		if(attachments.isEmpty()) {
			return new EmailMessage(from, subject, body, date,account, status);
		}else {
		return new EmailMessage(from, subject, body, date,account, status, attachments);
		}
	}

}
