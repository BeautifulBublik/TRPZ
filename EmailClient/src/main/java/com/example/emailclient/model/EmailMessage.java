package com.example.emailclient.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	    @Column(columnDefinition = "LONGTEXT")
	    private String body;
	    private Date date;
	    @Enumerated(EnumType.STRING)
	    private EmailStatus status; 
	    @OneToMany(cascade = CascadeType.ALL)
	    @JoinColumn(name="message_id")
	    private List<Attachment> attachments=new ArrayList<>();
	    @ManyToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "folder_id")
	    private Folder folder;
	    @ManyToOne
	    @JoinColumn(name = "account_id")
	    private Account account;
	    
		public EmailMessage(String from, String subject, String body, Date date, Account account) {
			super();
			this.from=from;
			this.subject = subject;
			this.body = body;
			this.date = date;
			this.account=account;
		}
		
		@Override
		public String toString() {
			return "Повідомлення: " + subject;
		}

		public EmailMessage(String from, String subject, String body, Date date,Account account, EmailStatus status,
				List<Attachment> attachments) {
			super();
			this.from = from;
			this.subject = subject;
			this.body = body;
			this.date = date;
			this.account=account;
			this.status = status;
			this.attachments = (attachments != null) ? attachments : new ArrayList<>();
		}

	    
	    
		public EmailMessage(String from, String subject, String body, Date date,Account account, EmailStatus status) {
			super();
			this.from = from;
			this.subject = subject;
			this.body = body;
			this.date = date;
			this.account=account;
			this.status = status;
		}
		



		public EmailMessage() {
			super();
		}




		public enum EmailStatus {
		    NEW,READ,SENT,DRAFT,DELETED,SPAM,FAILED      
		}

}
