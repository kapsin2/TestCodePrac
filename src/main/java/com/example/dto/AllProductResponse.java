package com.example.dto;

import com.example.entity.Client;
import com.example.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllProductResponse {
	private String productName;
	private String category;
	private int price;
	private String sellerName;

	public AllProductResponse(Product product, Client client) {
		this.productName = product.getProductName();
		this.category = client.getCategory();
		this.price = product.getPoint();
		this.sellerName = client.getUsername();
	}
}
