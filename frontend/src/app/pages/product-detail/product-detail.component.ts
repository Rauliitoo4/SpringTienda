import { Component, signal, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { Product } from '../../models/product/product.model';
import { Size } from '../../models/product/size.model';

@Component({
  selector: 'app-product-detail',
  imports: [MatButtonModule, MatIconModule, CommonModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  private productService = inject(ProductService);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  selectedSize = signal<Size | null>(null);
  isFavorite = signal(false);
  product = signal<Product | null>(null);
  loading = signal(true);
  error = signal(false);
  addedToCart = signal(false);
  quantity = signal(1);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getById(id).subscribe({
      next: (data) => {
        this.product.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  selectSize(size: Size) {
    this.selectedSize.set(size);
  }

  toggleFavorite() {
    this.isFavorite.update(v => !v);
  }

  increaseQuantity() {
    this.quantity.update(v => v + 1);
  }

  decreaseQuantity() {
    if (this.quantity() > 1) {
      this.quantity.update(v => v - 1);
    }
  }

  addToCart() {
    const user = this.authService.currentUser();

    if (!user) {
      this.router.navigate(['/auth']);
      return;
    }

    const product = this.product();
    const size = this.selectedSize();
    if (!product || !size) return;

    this.carritoService.addProduct(user.carritoId, product.id, this.quantity(), size).subscribe({
      next: () => {
        this.addedToCart.set(true);
        setTimeout(() => this.addedToCart.set(false), 2000);
      }
    });
  }
}