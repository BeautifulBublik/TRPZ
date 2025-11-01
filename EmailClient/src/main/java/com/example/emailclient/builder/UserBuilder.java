package com.example.emailclient.builder;

import java.util.List;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.EmailMessage;
import com.example.emailclient.model.User;


public class UserBuilder implements Builder<User> {
	private String name;
    private String password;
    private List<Account> accounts;
    
    public UserBuilder setName(String name) {
    	this.name=name;
    	return this;
    }
    public UserBuilder setPassword(String password) {
    	this.password=password;
    	return this;
    }
    public UserBuilder addAccountsAll(List<Account> accounts) {
    	this.accounts=accounts;
    	return this;
    }
    public UserBuilder addAccount(Account account) {
    	accounts.add(account);
    	return this;
    }

	@Override
	public User build() {
		
		return new User(name, password);
	}

}
