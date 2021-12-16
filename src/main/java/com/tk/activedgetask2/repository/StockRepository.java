package com.tk.activedgetask2.repository;

import com.tk.activedgetask2.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {




}
