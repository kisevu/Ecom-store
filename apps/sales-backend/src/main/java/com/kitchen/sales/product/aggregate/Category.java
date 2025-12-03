package com.kitchen.sales.product.aggregate;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.product.domain.vo.CategoryName;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public class Category {
  private final CategoryName name;
  private Long dbId;
  private PublicId publicId;

  public Category(CategoryName name, Long dbId, PublicId publicId) {
    assertMandatoryFields(name);
    this.name = name;
    this.dbId = dbId;
    this.publicId = publicId;
  }
  private void assertMandatoryFields(CategoryName name){
    Assert.notNull("name",name);
  }

  public void initializeDefaultFields(){
    this.publicId = new PublicId(UUID.randomUUID());
  }

  public CategoryName getName() {
    return name;
  }

  public Long getDbId() {
    return dbId;
  }

  public PublicId getPublicId() {
    return publicId;
  }
}

