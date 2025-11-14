package com.example.emailclient.decorator;

public class RetryMailSenderDecorator extends MailSenderDecorator {
	private int maxAttempts;

	public RetryMailSenderDecorator(EmailSender wrapped, int maxAttempts) {
		super(wrapped);
		this.maxAttempts=maxAttempts;
	}
	 @Override
	    public void sendEmail(String from, String password,
	                          String to, String subject, String body) {

	        int attempt = 0;

	        while (attempt < maxAttempts) {
	            attempt++;
	            try {
	                System.out.println("[RETRY] Спроба " + attempt);

	                super.sendEmail(from, password, to, subject, body);
	                return; 

	            } catch (Exception ex) {
	                System.err.println("[RETRY] Помилка: " + ex.getMessage());
	                if (attempt >= maxAttempts) {
	                    System.err.println("[RETRY] Вичерпано всі спроби");
	                    throw ex;
	                }
	            }
	        }
	    }
}
