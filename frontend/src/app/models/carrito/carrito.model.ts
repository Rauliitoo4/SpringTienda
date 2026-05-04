import { LineaCarrito } from "../carrito/linea-carrito.model";

export interface Carrito {
  id: number;
  total: number;
  lineas: LineaCarrito[];
}