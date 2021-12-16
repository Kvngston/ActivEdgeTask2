package com.tk.activedgetask2.security;

import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) throws IOException {
		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"result\":\"UNAUTHORIZED\",\"message\":\"Invalid Token, Full authentication required\"}");
	}
}
