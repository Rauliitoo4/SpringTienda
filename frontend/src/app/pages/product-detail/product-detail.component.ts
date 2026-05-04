import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule, Location } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product/product.service';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { UserService } from '../../services/user/user.service';
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
  private userService = inject(UserService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private location = inject(Location);

  selectedSize = signal<Size | null>(null);
  product = signal<Product | null>(null);
  loading = signal(true);
  error = signal(false);
  addedToCart = signal(false);
  quantity = signal(1);

  isFavorite = computed(() =>
    this.authService.currentUser()?.favoritoIds?.includes(this.product()?.id ?? 0) ?? false
  );

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
    const user = this.authService.currentUser();
    const product = this.product();
    if (!user || !product) {
      this.router.navigate(['/auth']);
      return;
    }

    if (this.isFavorite()) {
      this.userService.removeFavorito(user.id, product.id).subscribe({
        next: (updatedUser) => this.authService.currentUser.set(updatedUser)
      });
    } else {
      this.userService.addFavorito(user.id, product.id).subscribe({
        next: (updatedUser) => this.authService.currentUser.set(updatedUser)
      });
    }
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

  isNew(createdAt: string): boolean {
    const days = (Date.now() - new Date(createdAt).getTime()) / (1000 * 60 * 60 * 24);
    return days <= 15;
  }

  goBack() {
    this.location.back();
  }
}