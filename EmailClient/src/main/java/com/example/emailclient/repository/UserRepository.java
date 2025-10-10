package com.example.emailclient.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.emailclient.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.id = :id")
	Optional<User> findByIdWithAccounts(@Param("id")Long Id);
}
