package com.example.emailclient.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="message")
public class EmailMessage {
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;
		@Column(name = "from_email")
		private String from;
	    private String subject;
	    private String body;
	    private Date date;
	    @Enumerated(EnumType.STRING)
	    private EmailStatus status; 
	    @OneToMany(cascade = CascadeType.ALL)
	    @JoinColumn(name="message_id")
	    private List<Attachment> attachments;
	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "folder_id")
	    private Folder folder;
		public EmailMessage(String from, String subject, String body, Date date) {
			super();
			this.from=from;
			this.subject = subject;
			this.body = body;
			this.date = date;
		}
		
		@Override
		public String toString() {
			return "Повідомлення: " + subject;
		}

		public EmailMessage(String from, String subject, String body, Date date, EmailStatus status,
				List<Attachment> attachments) {
			super();
			this.from = from;
			this.subject = subject;
			this.body = body;
			this.date = date;
			this.status = status;
			this.attachments = attachments;
		}

	    
	    
		public EmailMessage(String from, String subject, String body, Date date, EmailStatus status) {
			super();
			this.from = from;
			this.subject = subject;
			this.body = body;
			this.date = date;
			this.status = status;
		}



		public enum EmailStatus {
		    NEW,READ,SENT,DRAFT,DELETED,SPAM,FAILED      
		}

}
