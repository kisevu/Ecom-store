package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.order.vo.OrderPrice;
import com.kitchen.sales.order.vo.OrderQuantity;
import com.kitchen.sales.order.vo.ProductPublicId;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.domain.vo.ProductName;
import org.jilt.Builder;

/**
 * Author: kev.Ameda
 */
@Builder
public class OrderedProduct {
  private final ProductPublicId productPublicId;
  private final OrderPrice price;
  private final OrderQuantity quantity;
  private final ProductName productName;


  public OrderedProduct(ProductPublicId productPublicId, OrderPrice price, OrderQuantity quantity, ProductName productName) {
    this.productPublicId = productPublicId;
    this.price = price;
    this.quantity = quantity;
    this.productName = productName;
  }

  public ProductPublicId getProductPublicId() {
    return productPublicId;
  }

  public OrderPrice getPrice() {
    return price;
  }

  public OrderQuantity getQuantity() {
    return quantity;
  }

  public ProductName getProductName() {
    return productName;
  }
  public static OrderedProduct create(long quantity, Product product){
    return OrderedProductBuilder.orderedProduct()
      .price(new OrderPrice(product.getProductPrice().value()))
      .quantity(new OrderQuantity(quantity))
      .productName(product.getProductName())
      .productPublicId(new ProductPublicId(product.getPublicId().value()))
      .build();
  }
}
