package com.kitchen.sales.order.service;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponseBuilder;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.aggregate.ProductCart;

import java.util.List;

/**
 * Author: kev.Ameda
 */
public class CartReader {

  public CartReader() {
  }

  public DetailCartResponse getDetails(List<Product> products){
    List<ProductCart> cartProducts = products.stream().map(ProductCart::from).toList();
    return DetailCartResponseBuilder.detailCartResponse()
      .productCarts(cartProducts)
      .build();
  }

}
