import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatBadgeModule } from '@angular/material/badge';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink, RouterLinkActive, MatIconModule, MatButtonModule, MatBadgeModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  isDarkMode = signal(false);
  cartItemCount = signal(0);

  toggleDarkMode() {
    this.isDarkMode.update(v => !v);
    document.body.classList.toggle('dark-theme', this.isDarkMode());
  }
}
