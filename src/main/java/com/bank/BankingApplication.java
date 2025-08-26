package com.bank;

import com.bank.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {
	public static void main(String[] args) {
		User user = new User();
		System.out.println("Loaded user: " + user.getEmail() + " with role: " + user.getRole());
		SpringApplication.run(BankingApplication.class, args);
	}
}