package com.kitchen.sales.product.domain.repository;

import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Author: kev.Ameda
 */
public interface ProductRepository {
  Product save(Product productToCreate);
  Page<Product> findAll(Pageable pageable);
  int delete(PublicId publicId);
  Page<Product> findAllFeaturedProduct(Pageable pageable);
  Optional<Product> findOne(PublicId publicId);
  Page<Product> findByCategoryExcludingOne(Pageable pageable, PublicId categoryPublicId, PublicId productPublicId);
}
