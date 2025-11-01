package com.example.emailclient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.emailclient.model.Account;
import com.example.emailclient.model.User;
import com.example.emailclient.builder.UserBuilder;
import com.example.emailclient.repository.AccountRepository;
import com.example.emailclient.repository.UserRepository;
import com.example.emailclient.service.providers.EmailProviderValidator;
import com.example.emailclient.service.providers.GmailValidator;
import com.example.emailclient.service.providers.IUaValidator;
import com.example.emailclient.service.providers.UkrNetValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	private UserRepository userRepo;
	private AccountRepository accountRepo;
	private AccountService accountService;
	
	
	
	public User createUser(String name, String password) {
		UserBuilder userBuilder=new UserBuilder();
		User user=userBuilder.setName(name)
				.setPassword(password)
				.build();
		return userRepo.save(user);
	}
	
	public List<User> getAllUsers() {
	    return userRepo.findAll();
	}
	@Transactional
	public User addAccount(Long userId, String email, 
			String password, String provider)	{ 
		User user=userRepo.findById(userId)
				.orElseThrow(()->new RuntimeException("Користувача не знайдено"));
		 EmailProviderValidator validator = getValidator(provider);
	        if (!validator.validate(email, password)) {
	            throw new RuntimeException("Помилка перевірки акаунту через " + provider);
	        }
		Account account =accountService.saveAccount(email, password, provider);
		user.addAccount(account);
		
		return userRepo.save(user);
	}
	private EmailProviderValidator getValidator(String provider) {
        return switch (provider.toLowerCase()) {
            case "gmail" -> new GmailValidator();
            case "ukr.net" -> new UkrNetValidator();
            case "i.ua" -> new IUaValidator();
            default -> throw new IllegalArgumentException("Невідомий провайдер: " + provider);
        };
    }
	@Transactional()
	public User getUserWithAccounts(Long userId) {
	    return userRepo.findByIdWithAccounts(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

}
