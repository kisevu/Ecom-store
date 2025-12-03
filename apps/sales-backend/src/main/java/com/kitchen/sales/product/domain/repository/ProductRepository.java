package com.kitchen.sales.product.domain.repository;

import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: kev.Ameda
 */
public interface ProductRepository {
  Product save(Product productToCreate);
  Page<Product> findAll(Pageable pageable);
  int delete(PublicId publicId);
}
