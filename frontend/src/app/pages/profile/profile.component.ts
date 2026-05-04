import { Component, signal, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/user/auth.service';
import { UserService } from '../../services/user/user.service';
import { ProductService } from '../../services/product/product.service';
import { User } from '../../models/user/user.model';
import { Product } from '../../models/product/product.model';

@Component({
  selector: 'app-profile',
  imports: [MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, MatTabsModule, FormsModule, CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private productService = inject(ProductService);
  private router = inject(Router);

  isEditing = signal(false);
  loading = signal(true);
  favorites = signal<Product[]>([]);

  user: User = {
    id: 0,
    name: '',
    lastname: '',
    username: '',
    email: '',
    carritoId: 0,
    favoritoIds: []
  };

  ngOnInit() {
    const currentUser = this.authService.currentUser();
    if (!currentUser) {
      this.router.navigate(['/auth']);
      return;
    }

    this.userService.getById(currentUser.id).subscribe({
      next: (data) => {
        this.user = { ...data };
        this.loading.set(false);
        this.loadFavorites(data.favoritoIds);
      }
    });
  }

  loadFavorites(ids: number[]) {
    if (!ids || ids.length === 0) return;
    this.productService.getAll().subscribe({
      next: (products) => {
        this.favorites.set(products.filter(p => ids.includes(p.id)));
      }
    });
  }

  toggleEdit() {
    this.isEditing.update(v => !v);
  }

  saveChanges() {
    this.userService.update(this.user.id, this.user).subscribe({
      next: (updated) => {
        this.authService.currentUser.set(updated);
        this.isEditing.set(false);
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/home']);
  }
}