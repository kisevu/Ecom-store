import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import { AdminService } from '../../service/admin-service';
import { ToastService } from '../../../shared/toast/service/toast-service';
import { Router } from '@angular/router';
import { CreateCategoryFormContent, ProductCategory } from '../../model/product.model';
import { injectMutation } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { NgxControlError } from 'ngxtension/control-error';


@Component({
  selector: 'ecom-create-category-component',
  imports: [CommonModule,ReactiveFormsModule,NgxControlError],
  templateUrl: './create-category-component.html',
  styleUrl: './create-category-component.scss',
})
export class CreateCategoryComponent {

  formBuilder = inject(FormBuilder);
  productService = inject(AdminService);
  toastService = inject(ToastService);
  router = inject(Router);

  name = new FormControl<string>('',{nonNullable:true,validators: [Validators.required]});

  public createForm = this.formBuilder.nonNullable.group<CreateCategoryFormContent>({
    name: this.name
  })

  loading = false;

  /*
   tanstack usage as the mutations are taken into consideration , caching and handling http requests
  */

  createMutation = injectMutation(()=> ({
    //lastValueFrom returns a promise and takes in an observabl
    mutationFn: (categoryToCreate:ProductCategory) => lastValueFrom(this.productService.createCategory(categoryToCreate)),
    // different hooks are called depending on the result
    onSettled: () => this.onCreationSettled(),
    onSuccess: () => this.onCreationSuccess(),
    onError: () => this.onCreationError()
  }));

  create(): void {
    const categoryToCreate: ProductCategory = {
      name: this.createForm.getRawValue().name
    };

    this.loading = true;
    //trigger the mutation
    this.createMutation.mutate(categoryToCreate);

  }

  private onCreationSettled(): void{
    this.loading = false;
  }

  private onCreationSuccess():void {
    this.toastService.show('category created','SUCCESS');
    this.router.navigate(['/admin/categories/list'])
  }

  private onCreationError():void {
    this.toastService.show('issue when creating category','ERROR');
  }
}
