import { Injectable, inject, signal, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap, firstValueFrom } from 'rxjs';
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

export interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private carritoService = inject(CarritoService);
  private apiUrl = 'http://localhost:8080';
  private platformId = inject(PLATFORM_ID);

  currentUser = signal<User | null>(null);
  isLoggedIn = signal(false);

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, data).pipe(
      tap(response => {
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('token', response.token);
          const userId = this.getUserIdFromToken(response.token);
          if (userId) {
            localStorage.setItem('userId', userId);
          }
        }
        this.isLoggedIn.set(true);
        this.loadUserFromStorage();
      })
    );
  }

  register(data: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/usuarios`, data).pipe(
      tap(user => {
        this.currentUser.set(user);
        this.isLoggedIn.set(true);
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('userId', String(user.id));
        }
      })
    );
  }

  logout() {
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.carritoService.carritoActual.set(null);
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
    }
  }

  loadUserFromStorage(): Promise<void> {
    if (!isPlatformBrowser(this.platformId)) return Promise.resolve();

    const userId = localStorage.getItem('userId');
    if (!userId) return Promise.resolve();

    return firstValueFrom(
      this.http.get<User>(`${this.apiUrl}/usuarios/${userId}`)
    ).then(user => {
      this.currentUser.set(user);
      this.isLoggedIn.set(true);
    }).catch(() => {
      this.logout();
    });
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    }
    return null;
  }

  private getUserIdFromToken(token: string): string | null {
    try {
      const payload = token.split('.')[1];
      const decoded = JSON.parse(atob(payload));
      return decoded.sub ?? null;
    } catch {
      return null;
    }
  }
}