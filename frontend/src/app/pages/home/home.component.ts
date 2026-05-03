import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  currentSlide = signal(0);

  products = [
    { id: 1, name: 'Camiseta lino oversize', category: 'CAMISETAS', price: 39.95, badge: 'NUEVO' },
    { id: 2, name: 'Pantalón wide leg', category: 'PANTALONES', price: 55.96, oldPrice: 69.95, badge: 'SALE' },
    { id: 3, name: 'Sudadera french terry', category: 'SUDADERAS', price: 59.95 },
    { id: 4, name: 'Camiseta algodón orgánico', category: 'CAMISETAS', price: 29.95, badge: 'NUEVO' },
    { id: 5, name: 'Chaqueta denim clásica', category: 'CHAQUETAS', price: 67.15, oldPrice: 79.95, badge: 'SALE' },
    { id: 6, name: 'Sudadera hoodie oversize', category: 'SUDADERAS', price: 69.95 },
  ];

  readonly visibleCount = 3;

  get totalSlides() {
    return this.products.length - this.visibleCount + 1;
  }

  get visibleProducts() {
    return this.products.slice(this.currentSlide(), this.currentSlide() + this.visibleCount);
  }

  get slidesArray() {
    return Array(this.totalSlides).fill(0);
  }

  prev() {
    if (this.currentSlide() === 0) {
      this.currentSlide.set(this.totalSlides - 1);
    } else {
      this.currentSlide.update(v => v - 1);
    }
  }

  next() {
    if (this.currentSlide() >= this.totalSlides - 1) {
      this.currentSlide.set(0);
    } else {
      this.currentSlide.update(v => v + 1);
    }
  }
}
