package com.tk.activedgetask2.controller;

import com.tk.activedgetask2.entity.dto.AuthRequest;
import com.tk.activedgetask2.entity.dto.AuthResponse;
import com.tk.activedgetask2.entity.dto.Response;
import com.tk.activedgetask2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest request){
		var response = new Response<AuthResponse>();
		var authResponse = userService.login(request);
		response.setResponseCode("00");
		response.setModelList(List.of(authResponse));
		response.setResponseMessage("Login Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
