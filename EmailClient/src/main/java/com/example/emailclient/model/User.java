package com.example.emailclient.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Data
@Entity
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    private String name;
    private String password;
    
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="user_id")
    private List<Account> accounts;
    
    
    public void addAccount(Account account) {
    	accounts.add(account);
    }


	@Override
	public String toString() {
		return "User: "+name;
	}


	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	
	
    
}
