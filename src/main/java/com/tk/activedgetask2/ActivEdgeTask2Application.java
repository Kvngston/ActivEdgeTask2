package com.tk.activedgetask2;

import com.tk.activedgetask2.entity.User;
import com.tk.activedgetask2.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ActivEdgeTask2Application implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ActivEdgeTask2Application.class, args);
	}

	@Override
	@SneakyThrows
	public void run(String... args) {

		if (userRepository.findByEmail("tochukwuchinedu21@gmail.com") == null){
			var admin = new User();
			admin.setEmail("tochukwuchinedu21@gmail.com");
			admin.setPassword(passwordEncoder.encode("test1234"));
			userRepository.save(admin);
		}
	}
}
