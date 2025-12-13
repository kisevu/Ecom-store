import { Component, effect, inject, OnInit } from '@angular/core';
import { CartService } from '../service/cart-service/cart-service';
import { Oauth2Service } from '../../auth/auth/service/oauth/oauth2-service';
import { ToastService } from '../../shared/toast/service/toast-service';
import { CartItem, CartItemAdd } from '../../shared/model/cart.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'ecom-cart-component',
  imports: [RouterLink],
  templateUrl: './cart-component.html',
  styleUrl: './cart-component.scss',
})
export class CartComponent implements OnInit {

  cartService = inject(CartService);

  oauth2Service = inject(Oauth2Service);

  toastService = inject(ToastService);

  cart: Array<CartItem> = [];

  labelCheckout = 'Login to checkout';

  action: 'login' | 'checkout' = 'login';


  cartQuery = injectQuery(() => ({
    queryKey: ['cart'],
    queryFn: () => lastValueFrom(this.cartService.getCartDetail()),
  }));

  private extractListToUpdate(){
    effect(()=>{
      if(this.cartQuery.isSuccess()){
        this.cart = this.cartQuery.data().products;
      }
    });
  }

  constructor(){
    this.extractListToUpdate();
    this.checkUserLoggedIn();
  }

  ngOnInit(): void {
    this.cartService.addedToCart.subscribe((cart) => this.updateQuantity(cart));
  }

  private checkUserLoggedIn(): void{
    const connectedUserQuery = this.oauth2Service.connectedUserQuery;
    if(connectedUserQuery?.isError()){
      this.labelCheckout = 'Logiin to checkout';
      this.action = 'login';
    } else if(connectedUserQuery?.isSuccess()){
      this.labelCheckout = 'checkout';
      this.action = 'checkout';
    }
  }

  private updateQuantity(cartUpdated: Array<CartItemAdd>) {
    for(const cartItemToUpdate of this.cart){
      const itemtToUpdate = cartUpdated.find((item) =>  item.publicId === cartItemToUpdate.publicId)
      if(itemtToUpdate){
        cartItemToUpdate.quantity = itemtToUpdate.quantity;
      } else {
        this.cart.splice(this.cart.indexOf(cartItemToUpdate),1);
      }
    }
  }

  addQuantityToCart(publicId:string){
    this.cartService.addToCart(publicId,'add');
  }

  removeQuantityFromCart(publicId:string){
    this.cartService.addToCart(publicId,'remove');
  }

  removeItem(publicId:string):void {
  const itemToRemoveIndex = this.cart.findIndex(item => item.publicId === publicId );
  if(itemToRemoveIndex){
    this.cart.splice(itemToRemoveIndex,1);
   }
   this.cartService.removeFromCart(publicId);
  }

  computeTotal(){
    return this.cart.reduce((acc,item)=> acc + item.price * item.quantity,0);
  }



}
