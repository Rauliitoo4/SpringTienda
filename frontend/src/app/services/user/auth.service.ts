import { Injectable, inject, signal, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, tap, firstValueFrom, switchMap, map } from 'rxjs';
import { User } from '../../models/user/user.model';
import { CarritoService } from '../carrito/carrito.service';
import { environment } from '../../../environments/environment';

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
  access_token: string;
  expires_in: number;
  refresh_token: string;
  token_type: string;
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
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('client_id', 'tienda-app');
    body.set('client_secret', environment.keycloakClientSecret);
    body.set('username', data.email);
    body.set('password', data.password);

    return this.http.post<LoginResponse>(
      'http://localhost:9090/realms/tienda/protocol/openid-connect/token',
      body.toString(),
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    ).pipe(
      tap(response => {
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('token', response.access_token);
          localStorage.setItem('userEmail', data.email);
        }
        this.isLoggedIn.set(true);
        this.loadUserFromStorage();
      })
    );
  }

  register(data: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/usuarios`, data).pipe(
      switchMap(user =>
        this.login({ email: data.email, password: data.password }).pipe(
          map(() => user)
        )
      )
    );
  }

  logout() {
    this.currentUser.set(null);
    this.isLoggedIn.set(false);
    this.carritoService.carritoActual.set(null);
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('userEmail');
    }
  }

  loadUserFromStorage(): Promise<void> {
    if (!isPlatformBrowser(this.platformId)) return Promise.resolve();

    const email = localStorage.getItem('userEmail');
    if (!email) return Promise.resolve();

    return firstValueFrom(
      this.http.get<User>(`${this.apiUrl}/usuarios/email/${email}`)
    ).then(user => {
      this.currentUser.set(user);
      this.isLoggedIn.set(true);
      if (isPlatformBrowser(this.platformId)) {
        localStorage.setItem('userId', String(user.id));
      }
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