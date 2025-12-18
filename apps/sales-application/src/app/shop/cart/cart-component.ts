import { Component, effect, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CartService } from '../service/cart-service/cart-service';
import { Oauth2Service } from '../../auth/auth/service/oauth/oauth2-service';
import { ToastService } from '../../shared/toast/service/toast-service';
import { CartItem, CartItemAdd, StripeSession } from '../../shared/model/cart.model';
import { injectMutation, injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { RouterLink } from "@angular/router";
import { CurrencyPipe, isPlatformBrowser } from '@angular/common';

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

  platformId = inject(PLATFORM_ID);


  cart: Array<CartItem> = [];

  labelCheckout = 'Login to checkout';
  action: 'login'| 'checkout' = 'login';

  isInitPaymentSessionLoading = false;

  cartQuery = injectQuery(() => ({
    queryKey: ['cart'],
    queryFn: () => lastValueFrom(this.cartService.getCartDetail())
  }));

  initPaymentSession = injectMutation(()=> ({
    mutationFn: (cart:Array<CartItemAdd>) => lastValueFrom(this.cartService.initPayment(cart)),
    onSuccess: (result:StripeSession) => this.onSessionCreateSuccess(result),
    onError: (error) => {
    console.error('Payment init failed:', error);
    this.isInitPaymentSessionLoading = false;
    this.toastService.show('Checkout failed. Please try again.', 'ERROR');
  }
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
   effect(()=>{
     const connectedUserQuery =  this.oauthService.connectedUserQuery;
    if(connectedUserQuery?.isError()){
      this.labelCheckout = 'Login to checkout';
      this.action = 'login';
    } else if(connectedUserQuery?.isSuccess()){
      this.labelCheckout = 'checkout';
      this.action = 'checkout';
    }
   });
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

  checkIfCartIsEmpty():boolean {
    if(isPlatformBrowser(this.platformId)){
      return this.cartQuery.isSuccess() && this.cartQuery.data().restProductCarts.length === 0;
    } else {
      return false;
    }
  }


  checkout() {
    if(this.action === 'login'){
      this.oauthService.login();
    } else if(this.action === 'checkout'){
      this.isInitPaymentSessionLoading = true;
      const cartItemsAdd = this.cart.map(item => ({publicId:item.publicId,quantity:item.quantity}) as CartItemAdd);
      this.initPaymentSession.mutate(cartItemsAdd);
    }
  }

  private onSessionCreateSuccess(stripeSession: StripeSession): void {
    this.cartService.storeSessionId(stripeSession.id);
    this.isInitPaymentSessionLoading = false;
    if(isPlatformBrowser(this.platformId) && stripeSession.url){
      window.location.href = stripeSession.url;
    }else {
      this.toastService.show(`Order error `,'ERROR');
    }
  }

}
