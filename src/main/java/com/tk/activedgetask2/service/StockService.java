package com.tk.activedgetask2.service;

import com.tk.activedgetask2.entity.Stock;
import com.tk.activedgetask2.entity.dto.StockRequest;

import java.util.List;

public interface StockService {

	Stock addStock(StockRequest request);

	Stock updateStock(long id, StockRequest request);

	Stock getById(long id);

	List<Stock> getAll(int pageNum, int pageSize);

	void deleteStock(long id);

}
