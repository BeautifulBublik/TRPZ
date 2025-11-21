package com.example.emailclient.service.sender;

import java.io.File;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractMailSender {
	private Session session;
    private Message message;
    private String from;
	public void sendEmail(String from, String password, String to, String subject, String body,List<File> attachments) {
		this.from = from;
		connect(from, password);
		prepareMessage(to, subject, body,attachments );
		send();
		disconnect();
	}

	public abstract void connect(String from, String password);

	public  void prepareMessage(String to, String subject, String body, List<File> attachments) {
		try {
            this.message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body, "UTF-8");
            multipart.addBodyPart(textPart);
            if (attachments != null) {
                for (File file : attachments) {
                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.attachFile(file);
                    multipart.addBodyPart(attachment);
                }
            }

            message.setContent(multipart);

        } catch (Exception e) {
            throw new RuntimeException("Помилка формування повідомлення", e);
        }
	}
	

	public  void send() {
	    try {
            Transport.send(message);
        } catch (Exception e) {
        	throw new RuntimeException("Не вдалося відправити лист", e);
        }
	}

	protected void disconnect() {
		System.out.println("Disconnected");
	}
}

