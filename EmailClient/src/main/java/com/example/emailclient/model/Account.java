package com.example.emailclient.model;
import lombok.Data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;


@Data
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    private String email;
    private String password;
    private String provider;
    @OneToMany
    @JoinColumn(name="Message_id")
    private List<EmailMessage> messages;
	@Override
	public String toString() {
		return "Account [email=" + email + "]";
	}

    
}
