package com.example.emailclient.service.sender;

import java.io.File;
import java.util.List;

public class LoggingMailSenderDecorator extends MailSenderDecorator {

	public LoggingMailSenderDecorator(EmailSender wrapped) {
		super(wrapped);
	}
	@Override
    public void sendEmail(String from, String password,
                          String to, String subject, String body,List<File> attachments) {
		System.out.println("[LOG] Надсилання листа:");
		System.out.println("  From: " + from);
		System.out.println("  To: " + to);
		System.out.println("  Subject: " + subject);

		try {
			super.sendEmail(from, password, to, subject, body, attachments);
			System.out.println("[LOG] Відправлено успішно.");
		} catch (Exception ex) {
			System.out.println("[LOG] ПОМИЛКА: Лист НЕ відправлено.");
			System.out.println("[LOG] Причина: " + ex.getMessage());
			throw ex; 
		}
	}

}
