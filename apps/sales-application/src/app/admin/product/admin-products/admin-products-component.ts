import { Component, effect, inject } from '@angular/core';
import { AdminService } from '../../service/admin-service';
import { ToastService } from '../../../shared/toast/service/toast-service';
import { injectMutation, injectQuery, injectQueryClient } from '@tanstack/angular-query-experimental';
import { Pagination } from '../../../shared/model/request.model';
import { lastValueFrom } from 'rxjs';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'ecom-admin-products-component',
  imports: [RouterLink, FaIconComponent, CommonModule],
  templateUrl: './admin-products-component.html',
  styleUrl: './admin-products-component.scss',
})
export class AdminProductsComponent {

  adminService = inject(AdminService);
  toastService = inject(ToastService);

  queryClient = injectQueryClient();

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: ['createdDate,desc']
  }


  constructor(){
    effect(()=> this.handleProductQueryError());
  }

  productQuery =injectQuery(()=>({
    queryKey: ['products',this.pageRequest],
    queryFn: () => lastValueFrom(this.adminService.findAllProducts(this.pageRequest))
  }));


   deleteMutation =  injectMutation(()=>({
    mutationFn: (productPublicId:string)=> lastValueFrom(this.adminService.deleteProduct(productPublicId)),
    onSuccess: () => this.onDeletionSuccess(),
    onError: () => this.onDeletionError()
   }));


   deleteProduct(publicId:string):void {
    this.deleteMutation.mutate(publicId);
   }


  private onDeletionSuccess(){
    this.queryClient.invalidateQueries({queryKey: ['products']});
    this.toastService.show('Product deleted','SUCCESS');
  }

  private onDeletionError(){
    this.toastService.show('Issue when deleting the product','ERROR');
  }

  private handleProductQueryError(){
    if(this.productQuery.isError()){
      this.toastService.show('Error failed to load products, please try again','ERROR');
    }
  }
}
