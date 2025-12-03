package com.kitchen.sales.product.infrastructure.secondary.entity;

import com.kitchen.sales.config.jpa.AbstractAuditingEntity;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.aggregate.ProductBuilder;
import com.kitchen.sales.product.domain.vo.*;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Entity
@Table(name = "product")
@Builder
public class ProductEntity extends AbstractAuditingEntity<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSequence")
  @SequenceGenerator(name = "productSequence",sequenceName = "product_sequence", allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @Column(name = "name",nullable = false)
  private String name;

  @Column(name = "color")
  private String color;

  @Column(name = "brand")
  private String brand;

  @Column(name = "description")
  private String description;

  @Column(name = "price")
  private double price;

  @Column(name = "featured")
  private boolean featured;

  @Enumerated(EnumType.STRING)
  @Column(name = "size")
  private ProductSize size;

  @Column(name = "publicId", unique = true)
  private UUID publicId;

  @Column(name = "nb_in_stock")
  private int nbInStock;

  @OneToMany(fetch = FetchType.LAZY,mappedBy = "product")
  private Set<PictureEntity> pictures = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "category_fk", referencedColumnName = "id")
  private CategoryEntity category;

  public ProductEntity() {
  }

  public ProductEntity(Long id, String name, String color,
                       String brand, String description, double price,
                       boolean featured,
                       ProductSize size, UUID publicId,
                       int nbInStock, Set<PictureEntity> pictures,
                       CategoryEntity category) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.brand = brand;
    this.description = description;
    this.price = price;
    this.featured = featured;
    this.size = size;
    this.publicId = publicId;
    this.nbInStock = nbInStock;
    this.pictures = pictures;
    this.category = category;
  }

  public static ProductEntity from( Product product ){
    ProductEntityBuilder productEntityBuilder = ProductEntityBuilder.productEntity();
    if(product.getDbId() != null){
      productEntityBuilder.id(product.getDbId());
    }
    return productEntityBuilder
      .brand(product.getProductBrand().value())
      .color(product.getProductColor().value())
      .description(product.getProductDescription().value())
      .name(product.getProductName().value())
      .price(product.getProductPrice().value())
      .size(product.getProductSize())
      .publicId(product.getPublicId().value())
      .category(CategoryEntity.from(product.getCategory()))
      .pictures(PictureEntity.from(product.getPictures()))
      .featured(product.getFeatured())
      .build();
  }

  public static Product to(ProductEntity productEntity){
    return ProductBuilder.product()
      .productBrand(new ProductBrand(productEntity.getBrand()))
      .productColor(new ProductColor(productEntity.getColor()))
      .productDescription(new ProductDescription(productEntity.getDescription()))
      .productName(new ProductName(productEntity.getName()))
      .productPrice(new ProductPrice(productEntity.getPrice()))
      .productSize(productEntity.getSize())
      .publicId(new PublicId(productEntity.getPublicId()))
      .category(CategoryEntity.to(productEntity.getCategory()))
      .pictures(PictureEntity.to(productEntity.getPictures()))
      .featured(productEntity.getFeatured())
      .numberInStock(productEntity.getNbInStock())
      .build();

  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean getFeatured() {
    return featured;
  }

  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  public ProductSize getSize() {
    return size;
  }

  public void setSize(ProductSize size) {
    this.size = size;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public int getNbInStock() {
    return nbInStock;
  }

  public void setNbInStock(int nbInStock) {
    this.nbInStock = nbInStock;
  }

  public Set<PictureEntity> getPictures() {
    return pictures;
  }

  public void setPictures(Set<PictureEntity> pictures) {
    this.pictures = pictures;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public void setCategory(CategoryEntity category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ProductEntity that)) return false;
    return Double.compare(getPrice(), that.getPrice()) == 0 && getFeatured() == that.getFeatured() && Objects.equals(getName(), that.getName()) && Objects.equals(getColor(), that.getColor()) && Objects.equals(getBrand(), that.getBrand()) && Objects.equals(getDescription(), that.getDescription()) && getSize() == that.getSize() && Objects.equals(getPublicId(), that.getPublicId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getColor(), getBrand(), getDescription(), getPrice(), getFeatured(), getSize(), getPublicId());
  }
}
