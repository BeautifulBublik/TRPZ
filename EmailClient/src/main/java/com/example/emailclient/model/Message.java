package com.example.emailclient.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;
	    private String subject;
	    private String body;
	    private LocalDateTime date=LocalDateTime.now();
	    private String status; 
	    @OneToMany()
	    @JoinColumn(name="message_id")
	    private List<Attachment> attachments;
	    @ManyToOne()
	    @JoinColumn(name = "folder_id")
	    private Folder folder;

}
