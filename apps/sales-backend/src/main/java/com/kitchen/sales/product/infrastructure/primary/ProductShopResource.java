package com.kitchen.sales.product.infrastructure.primary;

import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.application.ProductsApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/products-shop")
public class ProductShopResource {

  private final ProductsApplicationService productsApplicationService;

  public ProductShopResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @GetMapping("/featured")
  public ResponseEntity<Page<RestProduct>> getAllFeatured(Pageable pageable){
    Page<Product> products = productsApplicationService.getFeaturedProducts(pageable);
    Page<RestProduct> restProducts= new PageImpl<>(
      products.getContent().stream().map(RestProduct::fromDomain).toList(),
      pageable,
      products.getTotalElements()
    );
    return ResponseEntity.ok(restProducts);
  }

}
