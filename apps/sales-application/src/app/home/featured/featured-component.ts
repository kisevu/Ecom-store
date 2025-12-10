import { Component, inject } from '@angular/core';
import { UserProductService } from '../../shared/service/user-product/user-product-service';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { ProductCardComponent } from "../../shop/product-card/product-card-component";

@Component({
  selector: 'ecom-featured-component',
  imports: [ProductCardComponent],
  templateUrl: './featured-component.html',
  styleUrl: './featured-component.scss',
})
export class FeaturedComponent {

  productService = inject(UserProductService);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: []
  };

  featuredProductsQuery  = injectQuery(()=>({
    queryKey : ['featured-products',this.pageRequest],
    queryFn: () => lastValueFrom(this.productService.findAllFeaturedProducts(this.pageRequest)),
  }));

}
