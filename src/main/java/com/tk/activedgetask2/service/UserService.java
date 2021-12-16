package com.tk.activedgetask2.service;

import com.tk.activedgetask2.entity.User;
import com.tk.activedgetask2.entity.dto.AuthRequest;
import com.tk.activedgetask2.entity.dto.AuthResponse;

public interface UserService {
	AuthResponse login(AuthRequest request);
}
