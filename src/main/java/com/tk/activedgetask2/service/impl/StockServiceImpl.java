package com.tk.activedgetask2.service.impl;

import com.tk.activedgetask2.entity.Stock;
import com.tk.activedgetask2.entity.dto.StockRequest;
import com.tk.activedgetask2.exception.NotFoundException;
import com.tk.activedgetask2.repository.StockRepository;
import com.tk.activedgetask2.service.StockService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ModelMapper modelMapper;


	@Override
	public Stock addStock(StockRequest request) {
		modelMapper.typeMap(StockRequest.class, Stock.class)
				.addMappings(modelMapper -> modelMapper.skip(Stock::setCurrent_price));

		var stock = modelMapper.map(request, Stock.class);
		stock.setCurrent_price(Double.parseDouble(request.getCurrentPrice()));

		return stockRepository.save(stock);
	}

	@Override
	public Stock updateStock(long id, StockRequest request) {
		var stock = stockRepository.findById(id).orElseThrow(() -> new NotFoundException("Stock Not Found"));

		stock.setName(request.getName());
		stock.setCurrent_price(Double.parseDouble(request.getCurrentPrice()));
		return stockRepository.save(stock);
	}

	@Override
	public Stock getById(long id) {
		return stockRepository.findById(id).orElseThrow(() -> new NotFoundException("Stock Not Found"));
	}

	@Override
	public List<Stock> getAll(int pageNum, int pageSize) {
		List<Stock> stocks;
		var count = (int) stockRepository.count();
		Pageable pageable;
		if (count > 0) {
			if (pageNum > 0) {
				pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
			} else {
				pageable = PageRequest.of(0, (int) stockRepository.count(), Sort.by(Sort.Direction.DESC, "id"));
			}
			stocks = stockRepository.findAll(pageable).getContent();
		} else throw new NotFoundException("No result found");
		return stocks;
	}

	@Override
	public void deleteStock(long id) {
		var stock = stockRepository.findById(id).orElseThrow(() -> new NotFoundException("Stock Not Found"));
		stockRepository.delete(stock);
	}
}
