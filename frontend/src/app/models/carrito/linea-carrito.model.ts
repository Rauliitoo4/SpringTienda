import { Product } from "../product/product.model";
import { Size } from "../product/size.model";

export interface LineaCarrito {
  id: number;
  subtotal: number;
  quantity: number;
  size: Size;
  carritoId: number;
  productId: number;
  product: Product;
}