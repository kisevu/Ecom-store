import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";
import { FeaturedComponent } from "../featured/featured-component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'ecom-home-component',
  imports: [RouterLink, FeaturedComponent,CommonModule],
  templateUrl: './home-component.html',
  styleUrl: './home-component.scss',
})
export class HomeComponent {}
