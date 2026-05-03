import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-favorites',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './favorites.component.html',
  styleUrl: './favorites.component.scss'
})
export class FavoritesComponent {

  favorites = [
    { id: 1, name: 'Camiseta lino oversize', category: 'CAMISETAS', price: 39.95, badge: 'NUEVO' },
    { id: 2, name: 'Pantalón wide leg', category: 'PANTALONES', price: 55.96, oldPrice: 69.95, badge: 'SALE' },
    { id: 3, name: 'Sudadera french terry', category: 'SUDADERAS', price: 59.95 },
    { id: 4, name: 'Camiseta algodón orgánico', category: 'CAMISETAS', price: 29.95, badge: 'NUEVO' },
  ];

  removeFavorite(id: number) {
    this.favorites = this.favorites.filter(f => f.id !== id);
  }
}
