import { afterNextRender, Component, effect, inject } from '@angular/core';
import { injectQueryParams } from 'ngxtension/inject-query-params';
import { CartService } from '../service/cart-service/cart-service';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'ecom-cart-success-component',
  imports: [FaIconComponent],
  templateUrl: './cart-success-component.html',
  styleUrl: './cart-success-component.scss',
})
export class CartSuccessComponent {

  sessionId = injectQueryParams('session_id');

  cartService = inject(CartService);

  isValidAccess = true;

  constructor(){
    // afterNextRender(()=> this.verifySession());
    effect(()=>{
      this.verifySession()
    });
  }

  verifySession():void {
    const sessionIdLocalStorage = this.cartService.getSessionId();
    if(sessionIdLocalStorage !== this.sessionId()){
      this.isValidAccess = false;
    }else {
      this.cartService.deleteSessionId();
      this.cartService.clearCart();
    }
  }


}
