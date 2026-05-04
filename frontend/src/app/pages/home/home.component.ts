import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product/product.model';

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  private productService = inject(ProductService);

  currentSlide = signal(0);
  products = signal<Product[]>([]);

  readonly visibleCount = 3;

  get totalSlides(): number {
    return Math.ceil(this.products().length / this.visibleCount);
  }

  get visibleProducts(): Product[] {
    const start = this.currentSlide() * this.visibleCount;
    return this.products().slice(start, start + this.visibleCount);
  }

  get slidesArray(): number[] {
    return Array(this.totalSlides).fill(0);
  }

  ngOnInit(): void {
    this.productService.getAll().subscribe({
      next: (data) => this.products.set(data)
    });
  }

  prev(): void {
    if (this.currentSlide() === 0) {
      this.currentSlide.set(this.totalSlides - 1);
    } else {
      this.currentSlide.update(v => v - 1);
    }
  }

  next(): void {
    if (this.currentSlide() >= this.totalSlides - 1) {
      this.currentSlide.set(0);
    } else {
      this.currentSlide.update(v => v + 1);
    }
  }
}
