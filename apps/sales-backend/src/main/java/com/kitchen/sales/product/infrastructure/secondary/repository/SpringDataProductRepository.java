package com.kitchen.sales.product.infrastructure.secondary.repository;

import com.kitchen.sales.product.aggregate.Picture;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.repository.ProductRepository;
import com.kitchen.sales.product.domain.vo.PublicId;
import com.kitchen.sales.product.infrastructure.secondary.entity.CategoryEntity;
import com.kitchen.sales.product.infrastructure.secondary.entity.PictureEntity;
import com.kitchen.sales.product.infrastructure.secondary.entity.ProductEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Author: kev.Ameda
 */
@Repository
public class SpringDataProductRepository implements ProductRepository {

  private final JpaProductRepository productRepository;
  private final JpaCategoryRepository categoryRepository;
  private final JpaProductPictureRepository pictureRepository;

  public SpringDataProductRepository(JpaProductRepository productRepository,
                                     JpaCategoryRepository categoryRepository,
                                     JpaProductPictureRepository pictureRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.pictureRepository = pictureRepository;
  }

  @Override
  public Product save(Product productToCreate) {
    ProductEntity productEntityNew = ProductEntity.from(productToCreate);
    Optional<CategoryEntity> categoryEntityOpt = categoryRepository.findByPublicId(productEntityNew.getCategory().getPublicId());
    CategoryEntity categoryEntity = categoryEntityOpt.orElseThrow(() ->
      new EntityNotFoundException(String.format(" No category found for id: s%", productToCreate.getCategory().getPublicId())));
    productEntityNew.setCategory(categoryEntity);
    ProductEntity savedProductEntity = productRepository.save(productEntityNew);
    saveAllPictures(productToCreate.getPictures(),savedProductEntity);
    return ProductEntity.to(savedProductEntity);
  }

  private void saveAllPictures(List<Picture> pictures, ProductEntity productEntity){
    Set<PictureEntity> pictureEntities = PictureEntity.from(pictures);
    for (PictureEntity pictureEntity: pictureEntities){
      pictureEntity.setProduct(productEntity);
    }
    pictureRepository.saveAll(pictureEntities);
  }

  @Override
  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable).map(ProductEntity::to);
  }

  @Override
  public int delete(PublicId publicId) {
    return productRepository.deleteByPublicId(publicId.value());
  }
}
