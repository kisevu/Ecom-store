package com.kitchen.sales.product.application;

import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.aggregate.FilterQuery;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.repository.CategoryRepository;
import com.kitchen.sales.product.domain.repository.ProductRepository;
import com.kitchen.sales.product.domain.service.CategoryService;
import com.kitchen.sales.product.domain.service.ProductService;
import com.kitchen.sales.product.domain.service.ProductShop;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Author: kev.Ameda
 */
@Service
public class ProductsApplicationService {
  private CategoryService categoryService;
  private ProductService productService;
  private ProductShop productShop;

  public ProductsApplicationService(CategoryRepository categoryRepository,
                                    ProductRepository productRepository) {
    this.categoryService = new CategoryService(categoryRepository);
    this.productService = new ProductService(productRepository);
    this.productShop  = new ProductShop(productRepository);
  }

  @Transactional
  public Product createProduct(Product newProduct){
    return productService.save(newProduct);
  }

  @Transactional(readOnly = true)
  public Page<Product> findAllProducts(Pageable pageable){
    return productService.findAll(pageable);
  }

  @Transactional
  public PublicId deleteProduct(PublicId publicId){
    return productService.delete(publicId);
  }

  @Transactional
  public Category createCategory( Category category){
    return categoryService.save(category);
  }

  @Transactional(readOnly = true)
  public Page<Category> findAllCategories(Pageable pageable){
    return categoryService.findAll(pageable);
  }

  @Transactional
  public PublicId deleteCategory(PublicId publicId){
    return categoryService.delete(publicId);
  }

  @Transactional(readOnly = true)
  public Page<Product> getFeaturedProducts(Pageable pageable){
    return productShop.getFeaturedProducts(pageable);
  }

  @Transactional(readOnly = true)
  public Optional<Product> findOne(PublicId productPublicId){
    return productService.findOne(productPublicId);
  }

  @Transactional(readOnly = true)
  public Page<Product> findRelated(Pageable pageable, PublicId productPublicId){
    return productShop.findRelated(pageable, productPublicId);
  }

  @Transactional(readOnly = true)
  public Page<Product> filter(Pageable pageable, FilterQuery query){
    return productShop.filter(pageable,query);
  }

}
