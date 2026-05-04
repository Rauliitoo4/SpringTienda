import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product/product.model';

@Component({
  selector: 'app-catalog',
  imports: [RouterLink, MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, CommonModule, FormsModule],
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.scss'
})
export class CatalogComponent implements OnInit {
  private productService = inject(ProductService);

  selectedFilter = signal('todo');
  selectedSort = 'relevantes';
  products = signal<Product[]>([]);
  loading = signal(true);
  error = signal(false);
  currentPage = signal(0);
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

  get filteredProducts() {
    let list = this.products();
    if (this.selectedFilter() !== 'todo') {
      list = list.filter(p => p.category?.toLowerCase() === this.selectedFilter());
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
}