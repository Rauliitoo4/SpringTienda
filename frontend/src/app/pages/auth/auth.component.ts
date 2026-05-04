import { Component, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-auth',
  imports: [MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, FormsModule, CommonModule, RouterLink],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {
  isLogin = signal(true);
  showPassword = signal(false);

  loginData = { email: '', password: '' };
  registerData = { name: '', lastname: '', username: '', email: '', password: '' };

  toggleMode() {
    this.isLogin.update(v => !v);
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  login() { }

  register() { }
}