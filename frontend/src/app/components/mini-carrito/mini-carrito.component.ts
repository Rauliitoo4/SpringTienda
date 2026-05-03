import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mini-carrito',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './mini-carrito.component.html',
  styleUrl: './mini-carrito.component.scss'
})
export class MiniCarritoComponent {
  items = [
    { id: 1, name: 'Camiseta lino oversize', size: 'M', price: 39.95, quantity: 1 },
    { id: 2, name: 'Pantalón wide leg', size: 'S', price: 55.96, quantity: 2 },
    { id: 3, name: 'Sudadera french terry', size: 'L', price: 59.95, quantity: 1 },
    { id: 4, name: 'Chaqueta denim clásica', size: 'M', price: 67.15, quantity: 1 },
  ];

  get total() {
    return this.items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }
}
