import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product/product.service';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { UserService } from '../../services/user/user.service';
import { Product } from '../../models/product/product.model';
import { Size } from '../../models/product/size.model';
import { SearchbarComponent } from '../../components/searchbar/searchbar.component';
import { SizeSelectorComponent } from '../../components/size-selector/size-selector.component';

@Component({
  selector: 'app-catalog',
  imports: [RouterLink, MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, CommonModule, FormsModule, SearchbarComponent, SizeSelectorComponent],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent implements OnInit {
  private productService = inject(ProductService);
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private router = inject(Router);

  selectedFilter = signal('todo');
  selectedSort = signal('relevantes');
  searchQuery = signal('');
  products = signal<Product[]>([]);
  loading = signal(true);
  error = signal(false);
  currentPage = signal(0);
  selectedProduct = signal<Product | null>(null);
  showSizeSelector = signal(false);
  readonly pageSize = 9;

  ngOnInit() {
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
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

  get filteredProducts() {
    let list = this.products();

    if (this.selectedFilter() !== 'todo') {
      list = list.filter(p => p.category?.toLowerCase() === this.selectedFilter());
    }

    if (this.searchQuery()) {
      list = list.filter(p => p.name.toLowerCase().includes(this.searchQuery()));
    }

    switch (this.selectedSort()) {
      case 'precio-asc':
        list = [...list].sort((a, b) => a.finalPrice - b.finalPrice);
        break;
      case 'precio-desc':
        list = [...list].sort((a, b) => b.finalPrice - a.finalPrice);
        break;
      case 'novedades':
        list = [...list].sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        break;
      default:
        break;
    }

    return list;
  }

  get paginatedProducts() {
    const start = this.currentPage() * this.pageSize;
    return this.filteredProducts.slice(start, start + this.pageSize);
  }

  get totalPages() {
    return Math.ceil(this.filteredProducts.length / this.pageSize);
  }

  get pagesArray() {
    return Array(this.totalPages).fill(0);
  }

  setFilter(filter: string) {
    this.selectedFilter.set(filter);
    this.currentPage.set(0);
  }

  onSearch(query: string) {
    this.searchQuery.set(query.toLowerCase());
    this.currentPage.set(0);
  }

  goToPage(page: number) {
    this.currentPage.set(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  prevPage() {
    if (this.currentPage() > 0) {
      this.goToPage(this.currentPage() - 1);
    }
  }

  nextPage() {
    if (this.currentPage() < this.totalPages - 1) {
      this.goToPage(this.currentPage() + 1);
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
}