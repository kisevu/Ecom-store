package com.kitchen.sales.product.infrastructure.primary;

import com.kitchen.sales.product.aggregate.FilterQueryBuilder;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.application.ProductsApplicationService;
import com.kitchen.sales.product.domain.vo.ProductSize;
import com.kitchen.sales.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  @GetMapping("/find-one")
  public ResponseEntity<RestProduct> findOne(@RequestParam("publicId") UUID id){
    Optional<Product> productOpt = productsApplicationService.findOne(new PublicId(id));
    return productOpt.map(product -> ResponseEntity.ok(RestProduct.fromDomain(product)))
      .orElseGet(()-> ResponseEntity.badRequest().build());
  }

  @GetMapping("/related")
  public ResponseEntity<Page<RestProduct>> findRelated(Pageable pageable, @RequestParam("publicId") UUID id){
  try{
    Page<Product> products = productsApplicationService.findRelated(pageable, new PublicId(id));
    PageImpl<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::fromDomain).toList(),
      pageable,
      products.getTotalElements());
    return ResponseEntity.ok(restProducts);
  }catch (EntityNotFoundException ex){
    return ResponseEntity.badRequest().build();
  }
  }

  @GetMapping("/filter")
  public ResponseEntity<Page<RestProduct>> filterProducts(Pageable pageable,
                                                          @RequestParam("categoryId") UUID categoryId,
                                                          @RequestParam(value = "productSizes", required = false)List<ProductSize> productSizes){
    FilterQueryBuilder filterQueryBuilder = FilterQueryBuilder.filterQuery().categoryPublicId(new PublicId(categoryId));
    if(productSizes!=null){
      filterQueryBuilder.sizes(productSizes);
    }
    Page<Product> products = productsApplicationService.filter(pageable, filterQueryBuilder.build());
    PageImpl<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::fromDomain).toList(),
      pageable,
      products.getTotalElements());
    return ResponseEntity.ok(restProducts);
  }


}
