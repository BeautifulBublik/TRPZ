package com.example.emailclient.model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private EmailMessage message;

	public Attachment(String fileName, String filePath) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public Attachment() {
	}

	@Override
	public String toString() {
		return fileName;
	}
	
    
    
}
