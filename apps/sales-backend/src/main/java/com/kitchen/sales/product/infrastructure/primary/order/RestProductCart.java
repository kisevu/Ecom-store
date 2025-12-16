package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.product.aggregate.ProductCart;
import com.kitchen.sales.product.infrastructure.primary.RestPicture;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestProductCart(String name,
                              double price,
                              RestPicture picture,
                              int quantity,
                              UUID publicId,
                              String brand) {

  public static RestProductCart from(ProductCart productCart){
    return RestProductCartBuilder.restProductCart()
      .name(productCart.getName().value())
      .price(productCart.getPrice().value())
      .brand(productCart.getBrand().value())
      .picture(RestPicture.fromDomain(productCart.getPicture()))
      .publicId(productCart.getPublicId().value())
      .build();
  }

  public static List<RestProductCart> from(List<ProductCart> productCarts){
    return productCarts.stream()
      .map(RestProductCart::from)
      .toList();
  }
}
