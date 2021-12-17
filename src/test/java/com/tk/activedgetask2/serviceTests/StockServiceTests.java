package com.tk.activedgetask2.serviceTests;

import com.tk.activedgetask2.entity.Stock;
import com.tk.activedgetask2.entity.dto.StockRequest;
import com.tk.activedgetask2.exception.NotFoundException;
import com.tk.activedgetask2.repository.StockRepository;
import com.tk.activedgetask2.service.impl.StockServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class StockServiceTests {

	@InjectMocks
	private StockServiceImpl stockService;

	@Mock
	private StockRepository stockRepository;

	@Spy
	private ModelMapper modelMapper;

	private List<Stock> stocks;
	private Stock stock;
	private Stock updatedStock;
	private StockRequest stockRequest;

	@BeforeEach
	@SneakyThrows
	public void before(){

		stockRequest = new StockRequest();
		stockRequest.setName("test");
		stockRequest.setCurrentPrice("10000");

		stock = new Stock();
		stock.setName("test");
		stock.setCurrent_price(10000);


		updatedStock = new Stock();
		stock.setCurrent_price(90000);
		stock.setName("test updated");


		stocks = List.of(stock, updatedStock);
	}


	@Test
	public void addStockSuccessful(){
		when(stockRepository.save(any())).thenReturn(stock);
		var response = stockService.addStock(stockRequest);
		assert response == stock;
	}

	@Test
	public void getStockSuccessful(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
		var response = stockService.getById(1);
		assert response == stock;
	}

	@Test
	public void getStockNotFound(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> stockService.getById(1));
	}


	@Test
	public void getAllStocksSuccessful(){
		when(stockRepository.count()).thenReturn(Long.valueOf(stocks.size()));
		when(stockRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(stocks));
		var response = stockService.getAll(1,1);
		assertThat(response).isNotNull();
	}

	@Test
	public void getAllStocksNotResourceFound(){
		when(stockRepository.count()).thenReturn(Long.valueOf(0));
		assertThrows(NotFoundException.class, () -> stockService.getAll(1,1));
	}

	@Test
	public void updateStockSuccessful(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
		when(stockRepository.save(any())).thenReturn(updatedStock);
		var response = stockService.updateStock(1,stockRequest);
		assert response == updatedStock;
	}

	@Test
	public void updateStockNoStockFound(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> stockService.updateStock(1,stockRequest));
	}

	@Test
	public void deleteStockSuccessful(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
		stockService.deleteStock(1);
		verify(stockRepository, times(1)).delete(any());
	}

	@Test
	public void deleteStockNoStockFound(){
		when(stockRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> stockService.deleteStock(1));
	}

}
