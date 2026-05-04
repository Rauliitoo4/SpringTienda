import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product/product.service';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { UserService } from '../../services/user/user.service';
import { Product } from '../../models/product/product.model';
import { Size } from '../../models/product/size.model';
import { SizeSelectorComponent } from '../../components/size-selector/size-selector.component';

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule, SizeSelectorComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  private productService = inject(ProductService);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private router = inject(Router);

  currentSlide = signal(0);
  products = signal<Product[]>([]);
  selectedProduct = signal<Product | null>(null);
  showSizeSelector = signal(false);

  readonly visibleCount = 3;

  get totalSlides() {
    return Math.ceil(this.products().length / this.visibleCount);
  }

  get visibleProducts() {
    const start = this.currentSlide() * this.visibleCount;
    return this.products().slice(start, start + this.visibleCount);
  }

  get slidesArray() {
    return Array(this.totalSlides).fill(0);
  }

  ngOnInit() {
    this.productService.getAll().subscribe({
      next: (data) => this.products.set(data)
    });
  }

  isFavorite(productId: number): boolean {
    return this.authService.currentUser()?.favoritoIds?.includes(productId) ?? false;
  }

  toggleFavorite(product: Product) {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth']);
      return;
    }

    if (this.isFavorite(product.id)) {
      this.userService.removeFavorito(user.id, product.id).subscribe({
        next: (updatedUser) => this.authService.currentUser.set(updatedUser)
      });
    } else {
      this.userService.addFavorito(user.id, product.id).subscribe({
        next: (updatedUser) => this.authService.currentUser.set(updatedUser)
      });
    }
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

  openSizeSelector(product: Product) {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth']);
      return;
    }
    this.selectedProduct.set(product);
    this.showSizeSelector.set(true);
  }

  closeSizeSelector() {
    this.showSizeSelector.set(false);
    this.selectedProduct.set(null);
  }

  addToCart(event: { size: Size, quantity: number }) {
    const user = this.authService.currentUser();
    const product = this.selectedProduct();
    if (!user || !product) return;

    this.carritoService.addProduct(user.carritoId, product.id, event.quantity, event.size).subscribe({
      next: () => this.closeSizeSelector()
    });
  }

  isNew(createdAt: string): boolean {
    const days = (Date.now() - new Date(createdAt).getTime()) / (1000 * 60 * 60 * 24);
    return days <= 15;
  }
}