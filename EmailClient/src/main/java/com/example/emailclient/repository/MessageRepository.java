package com.example.emailclient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.EmailMessage;

public interface MessageRepository extends JpaRepository<EmailMessage, Long>  {

	List<EmailMessage> findByFolder_Id(Long id);

}
