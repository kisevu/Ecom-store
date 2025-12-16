import { Component, effect, inject, OnInit } from '@angular/core';
import { CartService } from '../service/cart-service/cart-service';
import { Oauth2Service } from '../../auth/auth/service/oauth/oauth2-service';
import { ToastService } from '../../shared/toast/service/toast-service';
import { CartItem, CartItemAdd } from '../../shared/model/cart.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { RouterLink } from "@angular/router";
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'ecom-cart-component',
  imports: [RouterLink,CurrencyPipe],
  templateUrl: './cart-component.html',
  styleUrl: './cart-component.scss',
})
export class CartComponent implements OnInit {

  cartService = inject(CartService);
  oauthService = inject(Oauth2Service);
  toastService = inject(ToastService);

  cart: Array<CartItem> = [];

  labelCheckout = 'Login to checkout';
  action: 'login'| 'checkout' = 'login';

  cartQuery = injectQuery(() => ({
    queryKey: ['cart'],
    queryFn: () => lastValueFrom(this.cartService.getCartDetail())
  }));

  constructor(){
    this.extractListToUpdate();
    this.checkUserLoggedIn();
  }

   private  extractListToUpdate(){
    effect(() => {
      if(this.cartQuery.isSuccess()){
        this.cart = this.cartQuery.data().restProductCarts;
      }
    });
  }

  private checkUserLoggedIn(){
    const connectedUserQuery =  this.oauthService.connectedUserQuery;
    if(connectedUserQuery?.isError()){
      this.labelCheckout = 'Login to checkout';
      this.action = 'login';
    } else if(connectedUserQuery?.isSuccess()){
      this.labelCheckout = 'checkout';
      this.action = 'checkout';
    }
  }


  ngOnInit(): void {
    this.cartService.addedToCart.subscribe( (cart) => {
      this.updateQuantity(cart);
    });
  }


  private updateQuantity(cartUpdated: Array<CartItemAdd>): void {
    for( const cartItemToUpdate of this.cart ){
      const itemToUpdate= cartUpdated.find(item => item.publicId === cartItemToUpdate.publicId);
      if(itemToUpdate){
        cartItemToUpdate.quantity =  itemToUpdate.quantity;
      } else {
        this.cart.splice(this.cart.indexOf(cartItemToUpdate),1);
      }
    }
  }

  addQuantityToCart(publicId:string): void {
    this.cartService.addToCart(publicId,'add');
  }

  reducingQuantityFromCart(publicId:string, quantity:number): void {
    if(quantity > 0 ){
      this.cartService.addToCart(publicId,'remove');
    }
  }

  removeItem(publicId:string):void {
    const itemToRemoveIndex = this.cart.findIndex(item => item.publicId === publicId);
    if(itemToRemoveIndex){
      this.cart.splice(itemToRemoveIndex,1);
    }else{
      this.cartService.removeFromCart(publicId);
    }
  }

  computeTotal(){
    return this.cart.reduce((acc,item) => acc + item.price * item.quantity,0);
  }



}
