import { Component, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { Oauth2Service } from '../../auth/auth/service/oauth/oauth2-service';
import { UserProductService } from '../../shared/service/user-product/user-product-service';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'ecom-navbar',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {

  oauth2Service  = inject(Oauth2Service);
  productService = inject(UserProductService);

  connectedUserQuery = this.oauth2Service.connectedUserQuery;
  categoryQuery = injectQuery(()=>({
    queryKey:['categories'],
    queryFn: () => lastValueFrom(this.productService.findAllCategories())
  }));

   login():void{
    this.closeDropDownMenu();
    this.oauth2Service.login();
   }

   logout():void{
    this.closeDropDownMenu();
    this.oauth2Service.logout();
   }

   isConnected():boolean{
    return this.connectedUserQuery?.status() === 'success'
    && this.connectedUserQuery?.data()?.email !== this.oauth2Service.notConnected;
   }

  closeDropDownMenu(){
    const bodyElement = document.activeElement as HTMLBodyElement;
    if(bodyElement){
      bodyElement.blur();
    }
  }

  closeMenu(menu: HTMLDetailsElement){
    menu.removeAttribute('open');
  }

}
