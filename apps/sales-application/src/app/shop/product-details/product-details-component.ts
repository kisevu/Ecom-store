import { Pagination } from './../../shared/model/request.model';
import { Component, effect, inject } from '@angular/core';
import { UserProductService } from '../../shared/service/user-product/user-product-service';
import { ToastService } from '../../shared/toast/service/toast-service';
import { injectParams } from 'ngxtension/inject-params';
import { Router } from '@angular/router';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { interval, lastValueFrom, take } from 'rxjs';
import { CurrencyPipe, NgStyle } from '@angular/common';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { ProductCardComponent } from '../product-card/product-card-component';
import { CartService } from '../service/cart-service/cart-service';
import { Product } from '../../admin/model/product.model';

@Component({
  selector: 'ecom-product-details-component',
  imports: [CurrencyPipe, NgStyle, FaIconComponent, ProductCardComponent],
  templateUrl: './product-details-component.html',
  styleUrl: './product-details-component.scss',
})
export class ProductDetailsComponent {

  publicId = injectParams('publicId');

  router = inject(Router);

  productService = inject(UserProductService);

  toastService = inject(ToastService);

  cartService = inject(CartService);

  lastPublicId = '';

  labelAddToCart = 'Add to cart';
  iconAddToCart = 'shopping-cart';

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: []
  };

  constructor(){
    effect(() =>  this.handlePublicIdChange());
    effect(()=> this.handleProductRelatedQueryError());
    effect(()=> this.handleProductQueryError());
  }

  productQuery = injectQuery(()=>({
    queryKey: ['product',this.publicId()],
    queryFn: () => lastValueFrom(this.productService.findOneByPublicId(this.publicId()!))
  }));


  relatedProductQuery = injectQuery(()=>({
    queryKey: ['related-product',this.publicId(), this.pageRequest],
    queryFn: () => lastValueFrom(this.productService.findRelated(this.pageRequest,this.publicId()!))
  }));


  private handlePublicIdChange():void {
    if(this.publicId()){
      // when the publicid signal is changing
      if(this.lastPublicId != this.publicId() && this.lastPublicId !== ''){
        this.relatedProductQuery.refetch();
        this.productQuery.refetch();
      }
      this.lastPublicId = this.publicId()!;
    }
  }

  private handleProductRelatedQueryError(): void {
    if(this.relatedProductQuery.isError()){
      this.toastService.show('Failed to load related products, try again later','ERROR');
    }
  }

  private handleProductQueryError():void {
    if(this.productQuery.isError()){
      this.toastService.show('Failed to load product, try again later','ERROR');
    }
  }

  addToCart(productToCart: Product){
    this.cartService.addToCart(productToCart.publicId,'add');
    this.labelAddToCart = 'Added to cart';
    this.iconAddToCart = 'check';
    interval(3000).pipe(
      take(1)  // not three events but 1
    ).subscribe(()=>{
      this.labelAddToCart = 'Add to cart';
      this.iconAddToCart = 'shopping-cart';
    });
  }

}
