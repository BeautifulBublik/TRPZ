package com.example.emailclient.service.receiver;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.emailclient.model.EmailMessage;

@Component
public class IUaPop3Receiv implements MailReceiver {

	@Override
	public List<EmailMessage> receiveEMails(String email, String password, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
