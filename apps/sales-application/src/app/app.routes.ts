import { Route } from '@angular/router';
import { AdminCategoriesComponent } from './admin/category/admin-categories/admin-categories-component';
import { CreateCategoryComponent } from './admin/category/create-category/create-category-component';
import { roleCheckGuard } from './auth/auth/service/guard/role-check.guard';



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
  }

  //timestamp 4.07
];
