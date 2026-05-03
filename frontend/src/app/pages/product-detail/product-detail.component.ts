import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  imports: [MatButtonModule, MatIconModule, CommonModule, RouterLink],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent {
  selectedSize = signal<string | null>(null);
  isFavorite = signal(false);

  product = {
    id: 1,
    name: 'Camiseta lino oversize',
    price: 39.95,
    finalPrice: 31.96,
    description: 'Camiseta de lino 100% de corte oversize. Perfecta para el día a día, con una caída natural y un tacto suave. Disponible en varios colores.',
    material: 'Lino 100%',
    considerations: 'Lavar a máquina a 30°. No usar secadora. Planchar a temperatura media.',
    imageUrl: null,
    sizes: ['XS', 'S', 'M', 'L', 'XL'],
    promotions: [{ description: 'Rebajas de verano', discount: 20 }]
  };

  selectSize(size: string) {
    this.selectedSize.set(size);
  }

  toggleFavorite() {
    this.isFavorite.update(v => !v);
  }
}
