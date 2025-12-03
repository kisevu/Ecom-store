package com.kitchen.sales.product.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.product.domain.vo.*;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public class Product {

  private final ProductBrand productBrand;
  private final ProductColor productColor;
  private final ProductDescription productDescription;
  private final ProductName productName;
  private final ProductPrice productPrice;
  private final ProductSize productSize;
  private final Category category;
  private final List<Picture> pictures;
  private Long dbId;
  private boolean featured;
  private PublicId publicId;
  private int numberInStock;

  public Product(ProductBrand productBrand, ProductColor productColor,
                 ProductDescription productDescription,
                 ProductName productName,
                 ProductPrice productPrice,
                 ProductSize productSize, Category category,
                 List<Picture> pictures, Long dbId,
                 boolean featured, PublicId publicId, int numberInStock) {
    assertMandatoryFields(productBrand,productColor,productDescription,productName,productPrice,productSize,category,pictures,featured,numberInStock);
    this.productBrand = productBrand;
    this.productColor = productColor;
    this.productDescription = productDescription;
    this.productName = productName;
    this.productPrice = productPrice;
    this.productSize = productSize;
    this.category = category;
    this.pictures = pictures;
    this.dbId = dbId;
    this.featured = featured;
    this.publicId = publicId;
    this.numberInStock = numberInStock;
  }
  private void assertMandatoryFields(ProductBrand brand,
                                     ProductColor color,
                                     ProductDescription description,
                                     ProductName name,
                                     ProductPrice price,
                                     ProductSize size,
                                     Category category,
                                     List<Picture> pictures,
                                     boolean featured,
                                     int numberInStock){

    Assert.notNull("brand",brand);
    Assert.notNull("color",color);
    Assert.notNull("description",description);
    Assert.notNull("name",name);
    Assert.notNull("price",price);
    Assert.notNull("size",size);
    Assert.notNull("category",category);
    Assert.notNull("pictures",pictures);
    Assert.notNull("featured",featured);
    Assert.notNull("numberInStock",numberInStock);
  }

  public void initDefaultFields(){
    this.publicId = new PublicId(UUID.randomUUID());
  }

  public ProductBrand getProductBrand() {
    return productBrand;
  }

  public ProductColor getProductColor() {
    return productColor;
  }

  public ProductDescription getProductDescription() {
    return productDescription;
  }

  public ProductName getProductName() {
    return productName;
  }

  public ProductPrice getProductPrice() {
    return productPrice;
  }

  public ProductSize getProductSize() {
    return productSize;
  }

  public Category getCategory() {
    return category;
  }

  public List<Picture> getPictures() {
    return pictures;
  }

  public Long getDbId() {
    return dbId;
  }

  public boolean getFeatured() {
    return featured;
  }

  public PublicId getPublicId() {
    return publicId;
  }

  public int getNumberInStock() {
    return numberInStock;
  }
}
