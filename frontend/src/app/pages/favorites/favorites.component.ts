import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/user/auth.service';
import { UserService } from '../../services/user/user.service';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product/product.model';
import { Size } from '../../models/product/size.model';
import { SizeSelectorComponent } from '../../components/size-selector/size-selector.component';
import { CarritoService } from '../../services/carrito/carrito.service';

@Component({
  selector: 'app-favorites',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule, SizeSelectorComponent],
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.scss'
})
export class FavoritesComponent implements OnInit {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private productService = inject(ProductService);
  private carritoService = inject(CarritoService);
  private router = inject(Router);

  favorites = signal<Product[]>([]);
  loading = signal(true);
  selectedProduct = signal<Product | null>(null);
  showSizeSelector = signal(false);

  ngOnInit() {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth']);
      return;
    }
    this.loadFavorites(user.favoritoIds);
  }

  loadFavorites(ids: number[]) {
    if (!ids || ids.length === 0) {
      this.loading.set(false);
      return;
    }
    this.productService.getAll().subscribe({
      next: (products) => {
        this.favorites.set(products.filter(p => ids.includes(p.id)));
        this.loading.set(false);
      }
    });
  }

  removeFavorite(productId: number) {
    const user = this.authService.currentUser();
    if (!user) return;
    this.userService.removeFavorito(user.id, productId).subscribe({
      next: (updatedUser) => {
        this.authService.currentUser.set(updatedUser);
        this.favorites.update(list => list.filter(p => p.id !== productId));
      }
    });
  }

  openSizeSelector(product: Product) {
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
}