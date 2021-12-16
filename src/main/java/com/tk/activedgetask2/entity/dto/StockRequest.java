package com.tk.activedgetask2.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class StockRequest {

	@NotBlank(message = "Name cannot be null")
	private String name;

	@NotBlank(message = "Current Price cannot be null")
	@Pattern(regexp = "[0-9]+", message = "Current price must be a number")
	private String currentPrice;

}
