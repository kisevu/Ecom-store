package com.kitchen.sales.product.domain.repository;

import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: kev.Ameda
 */
public interface CategoryRepository {
  Page<Category> findAll(Pageable pageable);
  int delete(PublicId publicId);
  Category save(Category categoryToCreate);
}
