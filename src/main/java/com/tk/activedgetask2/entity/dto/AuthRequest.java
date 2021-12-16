package com.tk.activedgetask2.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid Email")
	private String email;
	@NotBlank(message = "Password cannot be blank")
	private String password;

}
