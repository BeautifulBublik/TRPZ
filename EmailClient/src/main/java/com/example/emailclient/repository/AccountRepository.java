package com.example.emailclient.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.emailclient.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByEmail(String email);

}
