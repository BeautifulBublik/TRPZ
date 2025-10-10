package com.example.emailclient.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.User;
import com.example.emailclient.repository.AccountRepository;
import com.example.emailclient.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    @Transactional
    public void ValidateAccountNoRepeat(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = user.getAccounts().stream()
                .anyMatch(acc -> acc.getEmail().equalsIgnoreCase(email));

        if (exists) {
            throw new RuntimeException("Ця поштова адреса вже додана!");
        }
        
}
    public List<Account> getAccountsForUser(Long userId) {
        return userRepository.findByIdWithAccounts(userId)
                .map(User::getAccounts)
                .orElse(Collections.emptyList());
    }
}
