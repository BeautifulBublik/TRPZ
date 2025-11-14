package com.example.emailclient.decorator;

public abstract class MailSenderDecorator implements EmailSender {
	private  EmailSender wrapped;
	

	public MailSenderDecorator(EmailSender wrapped) {
		super();
		this.wrapped = wrapped;
	}


	@Override
	public void sendEmail(String fromEmail, String password, String toEmail, String subject, String body) {
		wrapped.sendEmail(fromEmail, password, toEmail, subject, body);
	}

}
