package com.tk.activedgetask2.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Stock extends AuditModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_generator")
	@SequenceGenerator(name = "stock-generator", sequenceName = "stock_seq")
	private long id;
	private String name;
	private double current_price;
}
