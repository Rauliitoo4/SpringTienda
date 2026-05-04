import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, MatTabsModule, FormsModule, CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  isEditing = signal(false);

  user = {
    name: 'Alberto',
    lastname: 'García',
    username: 'albertog',
    email: 'albertog@gmail.com',
    carritoId: 1
  };

  favorites = [
    { id: 1, name: 'Camiseta lino oversize', category: 'CAMISETAS', price: 39.95 },
    { id: 2, name: 'Pantalón wide leg', category: 'PANTALONES', price: 55.96 },
    { id: 3, name: 'Sudadera hoodie oversize', category: 'SUDADERAS', price: 69.95 },
  ];

  toggleEdit() {
    this.isEditing.update(v => !v);
  }

  saveChanges() {
    this.isEditing.set(false);
  }

  logout() { }
}