package com.kitchen.sales.product.infrastructure.primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.sales.product.aggregate.Product;
import com.kitchen.sales.product.application.ProductsApplicationService;
import com.kitchen.sales.product.domain.vo.PublicId;
import com.kitchen.sales.product.infrastructure.primary.exceptions.MultipartPictureException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/products")
public class ProductAdminResource {
  private final ProductsApplicationService productsApplicationService;
  public static final String ROLE_ADMIN ="ROLE_ADMIN";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Logger log = LoggerFactory.getLogger(ProductAdminResource.class);

  public ProductAdminResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @PreAuthorize("hasAnyRole('"+ ROLE_ADMIN +"')")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RestProduct> save( MultipartHttpServletRequest request ,
                                           @RequestPart("dto") String productRaw) throws JsonProcessingException {
    log.info("JSON: {}",productRaw);
    List<RestPicture> pictures = request.getFileMap()
      .values()
      .stream()
      .map(multipartFileToRestPicture())
      .toList();
    RestProduct restProduct = objectMapper.readValue(productRaw, RestProduct.class);
    restProduct.addPictureAttachment(pictures);
    Product product = RestProduct.toDomain(restProduct);
    Product savedProduct = productsApplicationService.createProduct(product);
    return ResponseEntity.ok(RestProduct.fromDomain(savedProduct));
  }

  /**
  * UUID is used with a purpose, we wouldn't want a scenario where the user is
   * aware of how many products we have in our db. With UUID we will not be able to iterate with url
   * later.
  * */

  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  @DeleteMapping
  public ResponseEntity<UUID> delete(@RequestParam("publicId") UUID id){
    try {
      PublicId deletedProductId = productsApplicationService.deleteProduct(new PublicId(id));
      return ResponseEntity.ok(deletedProductId.value());
    }catch (EntityNotFoundException ex){
      log.error("Could not delete product with id: {} , with error: {}", id, ex.getClass());
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
      return ResponseEntity.of(problemDetail).build();
    }
  }

  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  @GetMapping
  public ResponseEntity<Page<RestProduct>> getAll(Pageable pageable){
    Page<Product> products = productsApplicationService.findAllProducts(pageable);
    Page<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::fromDomain).toList(),
      pageable,
      products.getTotalElements()
    );
    return ResponseEntity.ok(restProducts);
  }

  private Function<MultipartFile, RestPicture> multipartFileToRestPicture(){
    return multipartFile -> {
      try{
        return new RestPicture(multipartFile.getBytes(), multipartFile.getContentType());
      }catch (IOException ex){
        throw new MultipartPictureException(String.format("Cannot pass the multipart file : %s",multipartFile.getOriginalFilename()));
      }
    };
  }


}
