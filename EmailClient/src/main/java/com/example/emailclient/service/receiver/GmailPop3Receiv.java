package com.example.emailclient.service.receiver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.Attachment;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.model.EmailMessage.EmailStatus;
import com.example.emailclient.service.AccountService;
import com.example.emailclient.service.EmailMessageService;
import com.example.emailclient.singelton.ConfigManager;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GmailPop3Receiv implements MailReceiver {
		

		private final EmailMessageService messageService;
		private final AccountService accountService;
		private List<EmailMessage> messagesList = new ArrayList<>();

		@Override
		public List<EmailMessage> receiveEMails(String email, String password, int limit)  {
			ConfigManager config = ConfigManager.getInstance();
	    	String protocol = config.getProperty("protocol.pop3");
	        String host = config.getProperty("gmail.pop3.host");
	        String port = config.getProperty("gmail.pop3.port");
	        String sslEnable= config.getProperty("mail.pop3s.ssl.enable");
	        String sslProtocols= config.getProperty("mail.pop3s.ssl.protocols");
	        
	        Properties props = new Properties();
	        props.put("mail.store.protocol", protocol);
	        props.put("mail.pop3s.host", host);
	        props.put("mail.pop3s.port", port);
	        props.put("mail.pop3s.ssl.enable", sslEnable);
	        props.put("mail.pop3s.ssl.protocols", sslProtocols);
			try {
				Session session = Session.getInstance(props);
				Store store = session.getStore("pop3s");
				store.connect(host, email, password);
				
				Folder inbox = store.getFolder("INBOX");
				inbox.open(Folder.READ_ONLY);
				int messageCount = inbox.getMessageCount();
	            int start = Math.max(1, messageCount - limit + 1);
	            Message[] messages = inbox.getMessages(start, messageCount);
				convertMessage(messages, email);

				inbox.close(false);
				store.close();
			} catch (Exception e) {
				System.err.println("Невірний логін або пароль Gmail (або заблоковано POP3-доступ)."+e.getMessage());
			}

			return messagesList;
			
		}
		
		private void convertMessage(Message[] messages, String email) throws Exception {

			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];
				EmailStatus status;
				String from = msg.getFrom() != null ? msg.getFrom()[0].toString() : "(невідомо)";
				String subject = msg.getSubject() != null ? msg.getSubject() : "(без теми)";
				Date date = msg.getSentDate();
				String content = MailUtils.extractTextFromMessage(msg);
				Flags flags = msg.getFlags();
				if (flags.contains(Flags.Flag.DELETED)) {
					status = EmailStatus.DELETED;
				} else if (flags.contains(Flags.Flag.DRAFT)) {
					status = EmailStatus.DRAFT;
				} else if (flags.contains(Flags.Flag.SEEN)) {
					status = EmailStatus.READ;
				} else {
					status = EmailStatus.NEW;
				}
				Account account=accountService.getAccountByEmail(email);
				List<Attachment> attachments = MailUtils.extractAttachments(msg, "downloads");
				if (attachments.isEmpty()) {
					messageService.saveMessage(from, subject, content, date,account, status);
				} else {
					messageService.saveMessageWithAttachment(from, subject, content, date,account, status, attachments);
				}
				
			}
		}
	}
		
