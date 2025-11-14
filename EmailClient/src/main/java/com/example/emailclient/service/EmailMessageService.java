package com.example.emailclient.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.emailclient.builder.EmailMessageBuilder;
import com.example.emailclient.builder.EmailMessageDirector;
import com.example.emailclient.model.Account;
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
			String body, Date date, Account account, EmailStatus status) {
		if(!isDuplicate(body, body, body, date)) {
		EmailMessageBuilder builder = new EmailMessageBuilder();
		EmailMessageDirector director = new EmailMessageDirector(builder);
		EmailMessage message=director.constructSimpleMessage(from, subject, body, date,account,  status);
		return messageRepository.save(message);
		}
		return null;
	}
	
	public EmailMessage saveMessageWithAttachment(String from, String subject, 
			String body, Date date,Account account, EmailStatus status, List<Attachment> attachments) {
		if(!isDuplicate(body, body, body, date)) {
		EmailMessageBuilder builder = new EmailMessageBuilder();
		EmailMessageDirector director = new EmailMessageDirector(builder);
		EmailMessage message=director.constructMessageWithAttachments(from, subject, body, date,account, status, attachments);
		return messageRepository.save(message);
	}
	return null;
	}
	public List<EmailMessage> findByAccountEmail(Account account){
		return messageRepository.findByAccount(account);
	}
	private boolean isDuplicate(String form,String subject, String body, Date date ) {
		Optional<EmailMessage> existing=messageRepository.findByFromAndSubjectAndBodyAndDate(form, subject, body, date);
		return existing.isPresent();
		
	}

	
}
