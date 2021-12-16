package com.tk.activedgetask2.service.impl;

import com.tk.activedgetask2.entity.dto.AuthRequest;
import com.tk.activedgetask2.entity.dto.AuthResponse;
import com.tk.activedgetask2.exception.SecurityException;
import com.tk.activedgetask2.security.CustomUserDetailsService;
import com.tk.activedgetask2.service.UserService;
import com.tk.activedgetask2.util.JwtUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	@SneakyThrows
	public AuthResponse login(AuthRequest request) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		}catch (BadCredentialsException e){
			throw new SecurityException("Invalid username and password");
		}

		var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		var jwt = jwtUtil.generateToken(userDetails);

		return new AuthResponse(jwt);
	}
}
