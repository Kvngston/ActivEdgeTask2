package com.tk.activedgetask2.controller;

import com.tk.activedgetask2.entity.Stock;
import com.tk.activedgetask2.entity.dto.Response;
import com.tk.activedgetask2.entity.dto.StockRequest;
import com.tk.activedgetask2.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {
		var response = new Response<Stock>();
		var stock = stockService.getById(id);
		response.setResponseCode("00");
		response.setModelList(List.of(stock));
		response.setResponseMessage("Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<?> getByAll(@RequestParam(name = "pageNum", defaultValue = "0") int page,
	                                  @RequestParam(name = "pageSize", defaultValue = "0") int size) {
		var response = new Response<Stock>();
		var stocks = stockService.getAll(page, size);
		response.setResponseCode("00");
		response.setModelList(stocks);
		response.setResponseMessage("Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PutMapping("/{id}")
	public ResponseEntity<?> updateStock(@PathVariable long id, @RequestBody @Valid StockRequest request) {
		var response = new Response<Stock>();
		var stock = stockService.updateStock(id, request);
		response.setResponseCode("00");
		response.setModelList(List.of(stock));
		response.setResponseMessage("Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<?> addStock(@RequestBody @Valid StockRequest request) {
		var response = new Response<Stock>();
		var stock = stockService.addStock(request);
		response.setResponseCode("00");
		response.setModelList(List.of(stock));
		response.setResponseMessage("Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		var response = new Response<Stock>();
		stockService.deleteStock(id);
		response.setResponseCode("00");
		response.setModelList(null);
		response.setResponseMessage("Successful");
		response.setErrors(null);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
