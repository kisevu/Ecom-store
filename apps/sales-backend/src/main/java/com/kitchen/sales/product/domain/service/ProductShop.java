package com.kitchen.sales.product.domain.service;

import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: kev.Ameda
 */
public class ProductShop {

  private final ProductRepository productRepository;

  public ProductShop(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<Product> getFeaturedProducts(Pageable pageable){
    return productRepository.findAllFeaturedProduct(pageable);
  }

}
