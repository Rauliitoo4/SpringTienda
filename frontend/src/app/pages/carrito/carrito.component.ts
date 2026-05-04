import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { Carrito } from '../../models/carrito/carrito.model';

@Component({
  selector: 'app-carrito',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.scss'
})
export class CarritoComponent implements OnInit {
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);

  carrito = signal<Carrito | null>(null);
  loading = signal(true);

  get items() {
    return this.carrito()?.lineas ?? [];
  }

  get total() {
    return this.carrito()?.total ?? 0;
  }

  get totalItems() {
    return this.items.reduce((acc, item) => acc + item.quantity, 0);
  }

  ngOnInit() {
    const user = this.authService.currentUser();
    if (user) {
      this.carritoService.getCarrito(user.carritoId).subscribe({
        next: (data) => {
          this.carrito.set(data);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    }
  }

  changeQuantity(lineaId: number, delta: number) {
    const linea = this.items.find(i => i.id === lineaId);
    if (!linea) return;
    const newQty = linea.quantity + delta;
    if (newQty < 1) {
      this.removeItem(lineaId);
      return;
    }
    this.carritoService.updateLinea(lineaId, newQty).subscribe({
      next: () => this.reload()
    });
  }

  removeItem(lineaId: number) {
    this.carritoService.deleteLinea(lineaId).subscribe({
      next: () => this.reload()
    });
  }

  private reload() {
    const user = this.authService.currentUser();
    if (user) {
      this.carritoService.getCarrito(user.carritoId).subscribe({
        next: (data) => this.carrito.set(data)
      });
    }
  }
}