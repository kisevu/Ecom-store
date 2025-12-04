package com.kitchen.sales.product.infrastructure.primary;

import com.kitchen.sales.config.error.domain.Assert;
import com.kitchen.sales.product.aggregate.Category;
import com.kitchen.sales.product.aggregate.CategoryBuilder;
import com.kitchen.sales.product.domain.vo.CategoryName;
import com.kitchen.sales.product.domain.vo.PublicId;
import org.jilt.Builder;
import java.util.UUID;

/**
 * Author: kev.Ameda
 */
@Builder
public record RestCategory(UUID publicId, String name) {

  public RestCategory {
    Assert.notNull("name",name);
  }

  public static Category toDomain(RestCategory restCategory){
    CategoryBuilder categoryBuilder = CategoryBuilder.category();
    if (restCategory.name != null ){
      categoryBuilder.name(new CategoryName(restCategory.name));
    }
    if(restCategory.publicId !=null ){
      categoryBuilder.publicId(new PublicId(restCategory.publicId));
    }
    return categoryBuilder.build();
  }

  public static RestCategory fromDomain(Category category){
    RestCategoryBuilder restCategoryBuilder = RestCategoryBuilder.restCategory();
    if(category.getName() !=null ){
      restCategoryBuilder.name(category.getName().value());
    }
    return restCategoryBuilder
      .publicId(category.getPublicId().value())
      .build();
  }

}
