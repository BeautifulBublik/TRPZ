package com.example.emailclient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>  {

	List<Message> findByFolder_Id(Long id);

}
