package com.kitchen.sales.product.domain.service;

import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.repository.ProductRepository;
import com.kitchen.sales.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Author: kev.Ameda
 */

public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product save(Product product){
    product.initDefaultFields();
    return productRepository.save(product);
  }

  public Page<Product> findAll(Pageable pageable){
    return productRepository.findAll(pageable);
  }

  public PublicId delete(PublicId productId){
    int numOfRowsDeleted = productRepository.delete(productId);
    if(numOfRowsDeleted!=1){
      throw new EntityNotFoundException(String.format(" Product not found with product id: %s",productId));
    }
    return productId;
  }


}
