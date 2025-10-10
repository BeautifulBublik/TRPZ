package com.example.emailclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.Draft;

public interface DraftRepository extends JpaRepository<Draft, Long> {

}
