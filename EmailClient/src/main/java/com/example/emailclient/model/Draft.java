package com.example.emailclient.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Draft extends Message {
	private LocalDateTime lastEdit=LocalDateTime.now();

	
	

}
