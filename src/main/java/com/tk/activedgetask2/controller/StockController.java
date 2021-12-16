package com.tk.activedgetask2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

	@GetMapping("/hello")
	public String hello(){
		return "hello";
	}
}
