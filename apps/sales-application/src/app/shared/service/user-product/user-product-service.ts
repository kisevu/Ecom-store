import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { createPaginationOption, Page, Pagination } from '../../model/request.model';
import { Product, ProductCategory, ProductFilter } from '../../../admin/model/product.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserProductService {

  http = inject(HttpClient);

  findAllFeaturedProducts(pageRequest: Pagination): Observable<Page<Product>>{
    const params = createPaginationOption(pageRequest);
    return this.http.get<Page<Product>> (`${environment.apiUrl}/products-shop/featured`,{params});
  }

  findOneByPublicId(publicId:string): Observable<Product>{
    return this.http.get<Product>(`${environment.apiUrl}/products-shop/find-one`,{params:{publicId}});
  }

  findRelated(pageRequest: Pagination, productPublicId: string) :Observable<Page<Product>>{
    let params = createPaginationOption(pageRequest);
    params = params.append('publicId', productPublicId);
    return this.http.get<Page<Product>>(`${environment.apiUrl}/products-shop/related`,{params});
  }

  findAllCategories(): Observable<Page<ProductCategory>>{
    return this.http.get<Page<ProductCategory>>(`${environment.apiUrl}/categories`);
  }


  filter(pageRequest: Pagination, filter: ProductFilter) : Observable<Page<Product>>{
    let params = createPaginationOption(pageRequest);

    if(filter.category){
     params = params.append('categoryId',filter.category);
    }

    if(filter.size){
      params = params.append('productSizes',filter.size);
    }

    return this.http.get<Page<Product>>(`${environment.apiUrl}/products-shop/filter`,{params});

  }

}
