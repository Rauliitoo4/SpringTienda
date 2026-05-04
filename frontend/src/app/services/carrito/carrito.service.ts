import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Carrito } from '../../models/carrito/carrito.model';
import { LineaCarrito } from '../../models/carrito/linea-carrito.model';
import { Size } from '../../models/product/size.model';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080';

  carritoActual = signal<Carrito | null>(null);

  getCarrito(id: number): Observable<Carrito> {
    return this.http.get<Carrito>(`${this.apiUrl}/carritos/${id}`).pipe(
      tap(data => this.carritoActual.set(data))
    );
  }

  addProduct(carritoId: number, productId: number, quantity: number, size: Size): Observable<Carrito> {
    return this.http.post<Carrito>(`${this.apiUrl}/carritos/${carritoId}/productos`, {
      productId,
      quantity,
      size
    }).pipe(
      tap(data => this.carritoActual.set(data))
    );
  }

  updateLinea(lineaId: number, quantity: number): Observable<LineaCarrito> {
    return this.http.put<LineaCarrito>(`${this.apiUrl}/lineas/${lineaId}`, { quantity });
  }

  deleteLinea(lineaId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/lineas/${lineaId}`);
  }
}