import { HttpClient } from '@angular/common/http';
import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Cart, CartItemAdd, StripeSession } from '../../../shared/model/cart.model';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CartService {

  platformId = inject(PLATFORM_ID);

  http = inject(HttpClient);

  private keyStorage = 'cart';

  // Behavior subject to prevent latency issues and sync with
  // all components that are receiving the data
  private addedToCart$ = new BehaviorSubject<Array<CartItemAdd>>([]);
  // addedToCart  observable is drawing from the addedToCart$ behavior
  //subject
  addedToCart = this.addedToCart$.asObservable();

  constructor(){
    const cartFromLocalstorage = this.getCartFromLocalStorage();
    this.addedToCart$.next(cartFromLocalstorage);
  }


  private getCartFromLocalStorage() : Array<CartItemAdd> {
    if (isPlatformBrowser(this.platformId)) {
      const cartProducts = localStorage.getItem(this.keyStorage);
      if (cartProducts) {
        return JSON.parse(cartProducts) as CartItemAdd[];
      } else {
        return [];
      }
    } else {
      return [];
    }
  }

  addToCart(publicId:string, command: 'add' | 'remove') : void {
    if(isPlatformBrowser(this.platformId)){
      const itemToAdd : CartItemAdd = {publicId,quantity: 1};
      const cartFromLocalstorage = this.getCartFromLocalStorage();
      if(cartFromLocalstorage.length !== 0 ){
        const productExists = cartFromLocalstorage.find( (item) => item.publicId === publicId);
        if(productExists){
          if(command === 'add'){
            productExists.quantity++;
          } else if(command === 'remove'){
            productExists.quantity--;
          }
        } else {
          cartFromLocalstorage.push(itemToAdd);
        }
      } else {
        cartFromLocalstorage.push(itemToAdd);
      }
      localStorage.setItem(this.keyStorage, JSON.stringify(cartFromLocalstorage));
      this.addedToCart$.next(cartFromLocalstorage);
    }
  }

  removeFromCart(publicId:string):void {
    if(isPlatformBrowser(this.platformId)){
     const cartFromLocalstorage =  this.getCartFromLocalStorage();
     const productExists = cartFromLocalstorage.find((item) => item.publicId  === publicId);
     if(productExists){
      cartFromLocalstorage.splice(cartFromLocalstorage.indexOf(productExists),1);
      localStorage.setItem(this.keyStorage, JSON.stringify(cartFromLocalstorage));
      this.addedToCart$.next(cartFromLocalstorage);
     }
    }
  }

   getCartDetail(): Observable<Cart> {
    const cartFromLocalStorage = this.getCartFromLocalStorage();
    const publicIdsForURL = cartFromLocalStorage.reduce(
      (acc, item) => `${acc}${acc.length > 0 ? ',' : ''}${item.publicId}`,
      '');
    return this.http.get<Cart>(`${environment.apiUrl}/orders/get-cart-details`, { params: { productIds: publicIdsForURL },})
      .pipe(
        map((cart) => this.mapQuantity(cart, cartFromLocalStorage)));
  }

   private mapQuantity(cart: Cart,
    cartFromLocalStorage: Array<CartItemAdd>): Cart {
    for (const cartItem of cartFromLocalStorage) {
      const foundProduct = cart.products.find( (item) => item.publicId === cartItem.publicId);
      if (foundProduct) {
        foundProduct.quantity = cartItem.quantity;
      }
    }
    return cart;
  }
}
