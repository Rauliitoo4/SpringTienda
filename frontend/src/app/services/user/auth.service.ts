import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../../models/user/user.model';
import { CarritoService } from '../carrito/carrito.service';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  lastname: string;
  username: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private apiUrl = 'http://localhost:8080';

  currentUser = signal<User | null>(null);
  isLoggedIn = signal(false);

  login(data: LoginRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/auth/login`, data).pipe(
      tap(user => {
        this.currentUser.set(user);
        this.isLoggedIn.set(true);
        localStorage.setItem('userId', String(user.id));
      })
    );
  }

  register(data: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/usuarios`, data).pipe(
      tap(user => {
        this.currentUser.set(user);
        this.isLoggedIn.set(true);
        localStorage.setItem('userId', String(user.id));
      })
    );
  }

  logout() {
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.carritoService.carritoActual.set(null);
    localStorage.removeItem('userId');
  }

  loadUserFromStorage(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.http.get<User>(`${this.apiUrl}/usuarios/${userId}`).subscribe({
        next: (user) => {
          this.currentUser.set(user);
          this.isLoggedIn.set(true);
        }
      });
    }
  }
}