import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../models/product/product.model';
import { ProductService } from '../../services/product/product.service';

@Component({
  selector: 'app-catalog',
  imports: [
    RouterLink, 
    MatButtonModule, 
    MatIconModule, 
    MatSelectModule, 
    MatFormFieldModule, 
    CommonModule, 
    FormsModule
  ],
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

  get filteredProducts() {
    let list = this.products();
    if (this.selectedFilter() !== 'todo') {
      list = list.filter(product => product.category?.toLowerCase() === this.selectedFilter());
    }
    return list;
  }

  ngOnInit(): void {
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

  setFilter(filter: string) {
    this.selectedFilter.set(filter);
  }
}
