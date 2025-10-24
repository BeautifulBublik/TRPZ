package com.example.emailclient.service.receiver;



import java.util.List;

import com.example.emailclient.model.EmailMessage;

public interface MailReceiverImap {
	List<EmailMessage> receiveEMails(String email, String password, int limit);	

}
