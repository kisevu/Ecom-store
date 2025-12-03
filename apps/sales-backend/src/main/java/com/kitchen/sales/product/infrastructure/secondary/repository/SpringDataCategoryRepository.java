package com.kitchen.sales.product.infrastructure.secondary.repository;

import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.domain.repository.CategoryRepository;
import com.kitchen.sales.product.domain.vo.PublicId;
import com.kitchen.sales.product.infrastructure.secondary.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Author: kev.Ameda
 */
@Repository
public class SpringDataCategoryRepository  implements CategoryRepository {

  private final JpaCategoryRepository categoryRepository;

  public SpringDataCategoryRepository(JpaCategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Page<Category> findAll(Pageable pageable) {
    return categoryRepository.
      findAll(pageable)
      .map(CategoryEntity::to);
  }

  @Override
  public int delete(PublicId publicId) {
    return categoryRepository.deleteByPublicId(publicId.value());
  }

  @Override
  public Category save(Category categoryToCreate) {
    CategoryEntity categoryToSave = CategoryEntity.from(categoryToCreate);
    CategoryEntity categorySaved = categoryRepository.save(categoryToSave);
    return CategoryEntity.to(categorySaved);
  }
}
