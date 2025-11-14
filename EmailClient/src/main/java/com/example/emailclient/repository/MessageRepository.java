package com.example.emailclient.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.EmailMessage;

public interface MessageRepository extends JpaRepository<EmailMessage, Long>  {

	List<EmailMessage> findByFolder_Id(Long id);
	Optional<EmailMessage> findByFromAndSubjectAndBodyAndDate(String form,String subject, 
			String body, Date date);
	List<EmailMessage> findByFrom(String email) ;
	List<EmailMessage> findByAccount(Account account);

}
