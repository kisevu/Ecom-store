package com.kitchen.sales.product.domain.service;

import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.domain.repository.CategoryRepository;
import com.kitchen.sales.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Author: kev.Ameda
 */
@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Category save(Category category){
    category.initializeDefaultFields();
    return categoryRepository.save(category);
  }

  Page<Category> findAll(Pageable pageable){
    return categoryRepository.findAll(pageable);
  }

  public PublicId delete(PublicId categoryId){
    int numRowsDeleted = categoryRepository.delete(categoryId);
    if (numRowsDeleted!=1){
      throw new EntityNotFoundException(String.format("No category is deleted with id %s",categoryId));
    }
    return categoryId;
  }
}
