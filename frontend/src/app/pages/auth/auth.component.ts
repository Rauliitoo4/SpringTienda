import { Component, signal, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/user/auth.service';

@Component({
  selector: 'app-auth',
  imports: [MatButtonModule, MatFormFieldModule, MatInputModule, MatIconModule, FormsModule, CommonModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class AuthComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  isLogin = signal(true);
  showPassword = signal(false);
  error = signal('');
  loading = signal(false);

  loginData = { email: '', password: '' };
  registerData = { name: '', lastname: '', username: '', email: '', password: '' };

  toggleMode() {
    this.isLogin.update(v => !v);
    this.error.set('');
  }

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  login() {
    if (!this.loginData.email || !this.loginData.password) {
      this.error.set('Por favor rellena todos los campos');
      return;
    }
    this.loading.set(true);
    this.authService.login(this.loginData).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/home']);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Email o contraseña incorrectos');
      }
    });
  }

  register() {
    if (!this.registerData.name || !this.registerData.email || !this.registerData.password) {
      this.error.set('Por favor rellena todos los campos');
      return;
    }
    this.loading.set(true);
    this.authService.register(this.registerData).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/home']);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Error al crear la cuenta. Inténtalo de nuevo.');
      }
    });
  }
}