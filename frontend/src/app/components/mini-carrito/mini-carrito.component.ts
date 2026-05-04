import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { CarritoService } from '../../services/carrito/carrito.service';
import { AuthService } from '../../services/user/auth.service';
import { Carrito } from '../../models/carrito/carrito.model';

@Component({
  selector: 'app-mini-carrito',
  imports: [RouterLink, MatButtonModule, MatIconModule, CommonModule],
  templateUrl: './mini-carrito.component.html',
  styleUrl: './mini-carrito.component.scss'
})
export class MiniCarritoComponent implements OnInit {
  private carritoService = inject(CarritoService);
  private authService = inject(AuthService);

  carrito = signal<Carrito | null>(null);

  get items() {
    return this.carrito()?.lineas ?? [];
  }

  get total() {
    return this.carrito()?.total ?? 0;
  }

  ngOnInit() {
    const user = this.authService.currentUser();
    if (user) {
      this.carritoService.getCarrito(user.carritoId).subscribe({
        next: (data) => this.carrito.set(data)
      });
    }
  }
}