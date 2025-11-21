package com.example.emailclient.service.sender;

import java.io.File;
import java.util.List;

public abstract class MailSenderDecorator implements EmailSender {
	private  EmailSender wrapped;
	

	public MailSenderDecorator(EmailSender wrapped) {
		super();
		this.wrapped = wrapped;
	}


	@Override
	public void sendEmail(String fromEmail, String password, String toEmail, 
			String subject, String body,List<File> attachments) {
		wrapped.sendEmail(fromEmail, password, toEmail, subject, body, attachments);
	}

}
