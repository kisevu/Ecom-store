import { Component, input } from '@angular/core';
import { Product } from '../../admin/model/product.model';
import { CommonModule } from '@angular/common';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'ecom-product-card-component',
  imports: [CommonModule, RouterLink],
  templateUrl: './product-card-component.html',
  styleUrl: './product-card-component.scss',
})
export class ProductCardComponent {

  // the new way of doing inputs.
  //  input as a signal below
  product = input.required<Product>();
}
