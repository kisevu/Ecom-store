import { Route } from '@angular/router';
import { AdminCategoriesComponent } from './admin/category/admin-categories/admin-categories-component';
import { CreateCategoryComponent } from './admin/category/create-category/create-category-component';
import { roleCheckGuard } from './auth/auth/service/guard/role-check.guard';
import { CreateProductsComponent } from './admin/product/create-products/create-products-component';
import { AdminProductsComponent } from './admin/product/admin-products/admin-products-component';
import { HomeComponent } from './home/home/home-component';
import { ProductDetailsComponent } from './shop/product-details/product-details-component';



export const appRoutes: Route[] = [

  {
    path: 'admin/categories/list',
    component: AdminCategoriesComponent,
    canActivate:[roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path:'admin/categories/create',
    component: CreateCategoryComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path:'admin/products/create',
    component: CreateProductsComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path:'admin/products/list',
    component: AdminProductsComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'product/:publicId',
    component: ProductDetailsComponent
  }
];
