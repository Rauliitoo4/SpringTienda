import { Component, signal, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatBadgeModule } from '@angular/material/badge';
import { CommonModule } from '@angular/common';
import { MiniCarritoComponent } from '../mini-carrito/mini-carrito.component';
import { AuthService } from '../../services/user/auth.service';
import { CarritoService } from '../../services/carrito/carrito.service';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink, RouterLinkActive, MatIconModule, MatButtonModule, MatBadgeModule, MiniCarritoComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private carritoService = inject(CarritoService);

  isDarkMode = signal(false);
  showCart = signal(false);

  get currentUser() {
    return this.authService.currentUser();
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  get favCount() {
    return this.authService.currentUser()?.favoritoIds?.length ?? 0;
  }

  get cartCount() {
    return this.carritoService.carritoActual()?.lineas?.length ?? 0;
  }

  formatBadge(count: number): string {
    return count > 9 ? '+9' : String(count);
  }

  onCartHover() {
    const user = this.authService.currentUser();
    if (user) {
      this.carritoService.getCarrito(user.carritoId).subscribe();
    }
    this.showCart.set(true);
  }

  toggleDarkMode() {
    this.isDarkMode.update(v => !v);
    document.documentElement.classList.toggle('dark-theme', this.isDarkMode());
  }
}