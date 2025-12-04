package com.kitchen.sales.product.infrastructure.primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.application.ProductsApplicationService;
import com.kitchen.sales.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

  private final ProductsApplicationService productsApplicationService;
  public static final String ROLE_ADMIN ="ROLE_ADMIN";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

  public CategoryResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  @PostMapping
  public ResponseEntity<RestCategory> save(@RequestBody RestCategory restCategory){
    Category category = RestCategory.toDomain(restCategory);
    Category savedCategory = productsApplicationService.createCategory(category);
    return ResponseEntity.ok(RestCategory.fromDomain(savedCategory));
  }

  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  @DeleteMapping
  public ResponseEntity<UUID> delete(@RequestParam("publicId") UUID id){
    try{
      PublicId deletedCategoryUUID = productsApplicationService.deleteCategory(new PublicId(id));
      return ResponseEntity.ok(deletedCategoryUUID.value());
    }catch (EntityNotFoundException ex){
      log.error("Could not delete category with id: {} , with error: {}", id, ex.getClass());
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
      return ResponseEntity.of(problemDetail).build();
    }
  }

  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  @GetMapping
  public ResponseEntity<Page<RestCategory>> findAll(Pageable pageable){
    Page<Category> allCategories = productsApplicationService.findAllCategories(pageable);
    Page<RestCategory> restCategories = new PageImpl<>(allCategories.getContent().stream().map(RestCategory::fromDomain).toList(),
      pageable,
      allCategories.getTotalElements());
    return ResponseEntity.ok(restCategories);
  }


}
