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
public record RestOrderReadAdmin(UUID publicId,
                                 OrderStatus orderStatus,
                                 List<RestOrderedItemRead> orderItems,
                                 String address,
                                 String email) {

  public static RestOrderReadAdmin from(Order order){
    StringBuilder sBuilder = new StringBuilder();
    if (order.getUser().getUserAddress()!=null){
      sBuilder.append(order.getUser().getUserAddress().street());
      sBuilder.append(", ");
      sBuilder.append(order.getUser().getUserAddress().zipcode());
      sBuilder.append(", ");
      sBuilder.append(order.getUser().getUserAddress().city());
      sBuilder.append(", ");
      sBuilder.append(order.getUser().getUserAddress().country());
      sBuilder.append(", ");
    }
    return RestOrderReadAdminBuilder.restOrderReadAdmin()
      .publicId(order.getPublicId().value())
      .orderStatus(order.getStatus())
      .orderItems(RestOrderedItemRead.from(order.getOrderedProducts()))
      .address(sBuilder.toString())
      .email(order.getUser().getEmail().value())
      .build();
  }
}
