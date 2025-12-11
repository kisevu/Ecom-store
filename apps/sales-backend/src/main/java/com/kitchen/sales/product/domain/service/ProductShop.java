package com.kitchen.sales.product.domain.service;

import com.kitchen.sales.product.aggregate.FilterQuery;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.repository.ProductRepository;
import com.kitchen.sales.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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

  public  Page<Product> findRelated(Pageable pageable, PublicId productPublicId){
    Optional<Product> productOpt= productRepository.findOne(productPublicId);
    if(productOpt.isPresent()){
      Product product = productOpt.get();
      return productRepository.findByCategoryExcludingOne(pageable, product.getCategory().getPublicId(), product.getPublicId());
    } else  {
      throw  new EntityNotFoundException(String.format("Not product found with product id : %s", productPublicId));
    }
  }

  public Page<Product> filter(Pageable pageable, FilterQuery query){
    return productRepository.findByCategoryAndSize(pageable,query);
  }

}
