package com.example.emailclient.service.receiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.singelton.ConfigManager;

public class IUaImapReceiv implements MailReceiverImap {
	@Override
	public List<EmailMessage> receiveEMails(String email, String password, int limit) {
		List<EmailMessage> messagesList = new ArrayList<>();
		ConfigManager config = ConfigManager.getInstance();
		String protocol = config.getProperty("protocol.imap");
		String host = config.getProperty("iua.imap.host");
		String port = config.getProperty("iua.imap.port");
		String sslEnable = config.getProperty("mail.imaps.ssl.enable");
		String sslProtocols = config.getProperty("mail.imaps.ssl.protocols");

		Properties props = new Properties();
		props.put("mail.store.protocol", protocol);
		props.put("mail.imaps.host", host);
		props.put("mail.imaps.port", port);
		props.put("mail.imaps.ssl.enable", sslEnable);
		props.put("mail.imaps.ssl.protocols", sslProtocols);
		try {
			Session session = Session.getInstance(props);
			Store store = session.getStore("imaps");
			store.connect(host, email, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();
			int start = Math.max(1, messageCount - limit + 1);
			Message[] messages = inbox.getMessages(start, messageCount);
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];

				String from = msg.getFrom() != null ? msg.getFrom()[0].toString() : "(невідомо)";
				String subject = msg.getSubject() != null ? msg.getSubject() : "(без теми)";
				Date date = msg.getSentDate();
				String content = extractTextFromMessage(msg);

				messagesList.add(new EmailMessage(from, subject, content, date));
			}

			inbox.close(false);
			store.close();
		} catch (Exception e) {
			System.err.println("Невірний логін або пароль Ukr.net (або заблоковано IMAP-доступ)."+e.getMessage());
		}

		return messagesList;

	}

	private String extractTextFromMessage(Message message) throws IOException, MessagingException {
		if (message.isMimeType("text/html")) {
			return cleanHtml((String) message.getContent());
		} else if (message.isMimeType("text/plain")) {
			return "<pre style='font-family:Segoe UI, sans-serif; white-space:pre-wrap;'>"
					+ message.getContent().toString() + "</pre>";
		} else if (message.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart part = multipart.getBodyPart(i);
				if (part.isMimeType("text/html")) {
					return cleanHtml((String) part.getContent());
				} else if (part.isMimeType("text/plain")) {
					return "<pre style='font-family:Segoe UI, sans-serif; white-space:pre-wrap;'>"
							+ part.getContent().toString() + "</pre>";
				}
			}
		}
		return "(немає тексту)";
	}

	private String cleanHtml(String html) {
		html = html.replaceAll("(?is)<(script|style|meta|link)[^>]*>.*?</\\1>", "");
		html = html.replaceAll("(?is)<(input|button|form|textarea)[^>]*>", "");
		html = html.replaceAll("(?i)on[a-z]+\\s*=\\s*['\"].*?['\"]", ""); 
		html = html.replaceAll("(?i)<html[^>]*>",
				"<html><body style='font-family:Segoe UI, sans-serif; font-size:13px; color:#333;'>");
		html += "</body></html>";
		return html;
	}

}
