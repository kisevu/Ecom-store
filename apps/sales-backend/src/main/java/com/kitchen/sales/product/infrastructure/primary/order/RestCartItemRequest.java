package com.kitchen.sales.product.infrastructure.primary.order;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequestBuilder;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestCartItemRequest(UUID publicId, long quantity) {

  public static DetailCartItemRequest to(RestCartItemRequest restCartItemRequest){
   return DetailCartItemRequestBuilder.detailCartItemRequest()
     .productId(new PublicId(restCartItemRequest.publicId()))
     .quantity(restCartItemRequest.quantity())
     .build();
  }

  public static RestCartItemRequest from(DetailCartItemRequest detailCartItemRequest){
    return RestCartItemRequestBuilder.restCartItemRequest()
      .publicId(detailCartItemRequest.productId().value())
      .quantity(detailCartItemRequest.quantity())
      .build();
  }

  public static List<DetailCartItemRequest> to(List<RestCartItemRequest> restCartItemRequests){
    return restCartItemRequests.stream().map(RestCartItemRequest::to).toList();
  }

  public static  List<RestCartItemRequest> from(List<DetailCartItemRequest> detailCartItemRequests){
    return detailCartItemRequests.stream().map(RestCartItemRequest::from).toList();
  }
}
