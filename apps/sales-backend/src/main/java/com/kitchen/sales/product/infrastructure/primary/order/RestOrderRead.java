package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.Order;
import com.kitchen.sales.order.vo.OrderStatus;
import org.jilt.Builder;
import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestOrderRead(UUID publicId,
                            OrderStatus orderStatus,
                            List<RestOrderedItemRead> orderedItems) {


  public static RestOrderRead from(Order order){
    return RestOrderReadBuilder.restOrderRead()
      .publicId(order.getPublicId().value())
      .orderStatus(order.getStatus())
      .orderedItems(RestOrderedItemRead.from(order.getOrderedProducts()))
      .build();
  }

}
