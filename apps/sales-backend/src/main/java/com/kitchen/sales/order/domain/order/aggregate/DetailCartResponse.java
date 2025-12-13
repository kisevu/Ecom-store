package com.kitchen.sales.order.domain.order.aggregate;

import com.kitchen.sales.product.aggregate.ProductCart;
import org.jilt.Builder;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Builder
public class DetailCartResponse {

  List<ProductCart> productCarts;

  public DetailCartResponse(List<ProductCart> productCarts) {
    this.productCarts = productCarts;
  }

  public List<ProductCart> getProductCarts() {
    return productCarts;
  }

  public void setProductCarts(List<ProductCart> productCarts) {
    this.productCarts = productCarts;
  }
}
