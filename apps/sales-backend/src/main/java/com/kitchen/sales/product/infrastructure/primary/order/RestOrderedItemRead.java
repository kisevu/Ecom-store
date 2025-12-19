package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.OrderedProduct;
import org.jilt.Builder;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestOrderedItemRead(long quantity,
                                  double price,
                                  String name) {

  public static RestOrderedItemRead from(OrderedProduct orderedProduct){
    return RestOrderedItemReadBuilder.restOrderedItemRead()
      .quantity(orderedProduct.getQuantity().value())
      .price(orderedProduct.getPrice().value())
      .name(orderedProduct.getProductName().value())
      .build();
  }

  public static List<RestOrderedItemRead> from(List<OrderedProduct> orderedProducts){
    return orderedProducts.stream().map(RestOrderedItemRead::from).toList();
  }

}
