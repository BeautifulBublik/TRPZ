package com.example.emailclient.service.receiver;



import java.util.List;

import com.example.emailclient.model.EmailMessage;

public interface MailReceiver {
	List<EmailMessage> receiveEMails(String email, String password, int limit);	

}
