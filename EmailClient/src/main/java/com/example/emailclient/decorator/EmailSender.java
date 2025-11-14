package com.example.emailclient.decorator;

public interface EmailSender {
	void sendEmail(String fromEmail, String password,
			String toEmail, String subject, String body);
}
