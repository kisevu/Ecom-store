import { Component, effect, inject } from '@angular/core';
import { RouterLink } from "@angular/router";
import { AdminService } from '../../service/admin-service';
import { injectMutation, injectQuery, QueryClient } from '@tanstack/angular-query-experimental';
import { ToastService } from '../../../shared/toast/service/toast-service';
import { lastValueFrom } from 'rxjs';
import { FaIconComponent } from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'ecom-admin-categories-component',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './admin-categories-component.html',
  styleUrl: './admin-categories-component.scss',
})
export class AdminCategoriesComponent {

  adminService = inject(AdminService);
  queryClient = inject(QueryClient);
  toastService = inject(ToastService);

  constructor(){
    effect(()=> this.handleCategoriesQueryError());
  }

  categoriesQuery =injectQuery(()=>({
    queryKey: ['categories'],
    queryFn: () => lastValueFrom(this.adminService.findAllCategories())
  }));

  deleteMutation = injectMutation(()=>({
    mutationFn: (categoryPublicId: string) =>  lastValueFrom(this.adminService.deleteCategory(categoryPublicId)),
    onSuccess: () => this.onDeletionSuccess(),
    onError: ()=> this.onDeletionError()
  }));

  private onDeletionSuccess(): void {
    this.queryClient.invalidateQueries({queryKey: ['categories']});
    this.toastService.show('Category deleted','SUCCESS');
  }

  private onDeletionError(): void {
    this.toastService.show('Issue when deleting category','ERROR');
  }

  private handleCategoriesQueryError(): void {
    if(this.categoriesQuery.isError()){
      this.toastService.show('Error! failed to load categories. Try again later','ERROR');
    }
  }

  deleteCategory(publicId: string) {
    this.deleteMutation.mutate(publicId);
   }

}
