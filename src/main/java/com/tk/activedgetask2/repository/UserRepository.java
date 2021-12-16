package com.tk.activedgetask2.repository;

import com.tk.activedgetask2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
