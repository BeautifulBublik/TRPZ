package com.example.emailclient.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Draft extends EmailMessage {
	public Draft(String from ,String subject, String body, Date date) {
		super(from, subject, body, date);
	}

	private LocalDateTime lastEdit=LocalDateTime.now();

	
	

}
