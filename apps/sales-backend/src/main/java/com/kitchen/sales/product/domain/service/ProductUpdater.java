package com.kitchen.sales.product.domain.service;

import com.kitchen.sales.order.domain.order.aggregate.OrderProductQuantity;
import com.kitchen.sales.product.domain.repository.ProductRepository;

import java.util.List;

/**
 * Author: kev.Ameda
 */
public class ProductUpdater {

  private final ProductRepository productRepository;

  public ProductUpdater(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void updateQuantity(List<OrderProductQuantity> orderProductQuantities){
    for (OrderProductQuantity orderProductQuantity: orderProductQuantities){
      productRepository.updateQuantity(orderProductQuantity.productPublicId(),orderProductQuantity.orderQuantity().value());
    }
  }

}
