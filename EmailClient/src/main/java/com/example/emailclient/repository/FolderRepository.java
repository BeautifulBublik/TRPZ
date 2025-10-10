package com.example.emailclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
