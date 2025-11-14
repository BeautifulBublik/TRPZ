package com.example.emailclient.decorator;

public class LoggingMailSenderDecorator extends MailSenderDecorator {

	public LoggingMailSenderDecorator(EmailSender wrapped) {
		super(wrapped);
	}
	@Override
    public void sendEmail(String from, String password,
                          String to, String subject, String body) {
		System.out.println("[LOG] Надсилання листа:");
		System.out.println("  From: " + from);
		System.out.println("  To: " + to);
		System.out.println("  Subject: " + subject);

		try {
			super.sendEmail(from, password, to, subject, body);
			System.out.println("[LOG] Відправлено успішно.");
		} catch (Exception ex) {
			System.out.println("[LOG] ПОМИЛКА: Лист НЕ відправлено.");
			System.out.println("[LOG] Причина: " + ex.getMessage());
			throw ex; 
		}
	}

}
