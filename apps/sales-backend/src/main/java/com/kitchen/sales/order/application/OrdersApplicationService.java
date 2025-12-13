package com.kitchen.sales.order.application;

import com.kitchen.sales.order.domain.order.aggregate.DetailCartItemRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartRequest;
import com.kitchen.sales.order.domain.order.aggregate.DetailCartResponse;
import com.kitchen.sales.order.service.CartReader;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.application.ProductsApplicationService;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: kev.Ameda
 */
@Service
public class OrdersApplicationService {

  private final ProductsApplicationService productsApplicationService;
  private final CartReader cartReader;


  public OrdersApplicationService(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
    this.cartReader = new CartReader();
  }

  @Transactional(readOnly = true)
  public DetailCartResponse getCartDetails(DetailCartRequest detailCartRequest){
    List<PublicId> publicIds = detailCartRequest.items()
      .stream()
      .map(DetailCartItemRequest::productId)
      .toList();
    List<Product> productInformation = productsApplicationService.getProductByPublicIdIn(publicIds);
    return cartReader.getDetails(productInformation);
  }

}
