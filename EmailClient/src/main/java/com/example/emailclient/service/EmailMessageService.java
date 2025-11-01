package com.example.emailclient.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.emailclient.builder.EmailMessageBuilder;
import com.example.emailclient.builder.EmailMessageDirector;
import com.example.emailclient.model.Attachment;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.model.EmailMessage.EmailStatus;
import com.example.emailclient.repository.MessageRepository;


import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class EmailMessageService {
	MessageRepository messageRepository;
	
	public EmailMessage saveMessage(String from, String subject, 
			String body, Date date, EmailStatus status) {
		EmailMessageBuilder builder = new EmailMessageBuilder();
		EmailMessageDirector director = new EmailMessageDirector(builder);
		EmailMessage message=director.constructSimpleMessage(from, subject, body, date, status);
		return messageRepository.save(message);
	}
	
	public EmailMessage saveMessageWithAttachment(String from, String subject, 
			String body, Date date, EmailStatus status, List<Attachment> attachments) {
		EmailMessageBuilder builder = new EmailMessageBuilder();
		EmailMessageDirector director = new EmailMessageDirector(builder);
		EmailMessage message=director.constructMessageWithAttachments(from, subject, body, date, status, attachments);
		return messageRepository.save(message);
	}
}
