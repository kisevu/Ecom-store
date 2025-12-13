package com.kitchen.sales.product.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.product.domain.vo.ProductBrand;
import com.kitchen.sales.product.domain.vo.ProductName;
import com.kitchen.sales.product.domain.vo.ProductPrice;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public class ProductCart {
  private ProductName name;

  private ProductPrice price;

  private ProductBrand brand;

  private Picture picture;

  private PublicId publicId;

  public ProductCart() {
  }

  public ProductCart(ProductName name, ProductPrice price, ProductBrand brand, Picture picture, PublicId publicId) {
    assertFields(name, price,brand,picture,publicId);
    this.name = name;
    this.price = price;
    this.brand = brand;
    this.picture = picture;
    this.publicId = publicId;
  }

  private void assertFields(ProductName name, ProductPrice price, ProductBrand brand, Picture picture, PublicId publicId){
    Assert.notNull("name",name);
    Assert.notNull("price", price);
    Assert.notNull("picture",picture);
    Assert.notNull("brand",brand);
    Assert.notNull("productId",publicId);
  }

  public static ProductCart from(Product product){
    return ProductCartBuilder.productCart()
      .name(product.getProductName())
      .price(product.getProductPrice())
      .picture(product.getPictures().stream().findFirst().orElseThrow())
      .publicId(product.getPublicId())
      .brand(product.getProductBrand())
      .build();
  }

  public ProductName getName() {
    return name;
  }

  public ProductPrice getPrice() {
    return price;
  }

  public ProductBrand getBrand() {
    return brand;
  }

  public Picture getPicture() {
    return picture;
  }

  public PublicId getPublicId() {
    return publicId;
  }
}
