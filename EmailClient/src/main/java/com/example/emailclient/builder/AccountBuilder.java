package com.example.emailclient.builder;
import java.util.List;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.EmailMessage;

public class AccountBuilder implements Builder<Account> {
	private String email;
    private String password;
    private String provider;
    private List<EmailMessage> messages;

    public AccountBuilder setEmail(String email) {
    	this.email=email;
    	return this;
    }
    public AccountBuilder setPassword(String password) {
    	this.password=password;
    	return this;
    }
    public AccountBuilder setProvider(String provider) {
    	this.provider=provider;
    	return this;
    }
    public AccountBuilder addMessageAll(List<EmailMessage> messages) {
    	this.messages=messages;
    	return this;
    }
    public AccountBuilder addMessage(EmailMessage message) {
    	messages.add(message);
    	return this;
    }
	@Override
	public Account build() {
		if(messages.isEmpty()) {
		return new Account(email, password, provider);
		}else {
			return new Account(email, password, provider, messages);
		}
	}

}
