package com.example.emailclient.service.sender;

import java.io.File;
import java.util.List;

public interface EmailSender {
	void sendEmail(String from, String password,
			String to, String subject, String body, List<File> attachments);
}
