import { Promotion } from '../promotion/promotion.model';
import { Size } from './size.model';
import { Category } from './category.model';

export interface Product {
  id: number;
  name: string;
  price: number;
  finalPrice: number;
  description: string;
  material: string;
  considerations: string;
  imageUrl: string;
  promotions: Promotion[];
  sizes: Size[];
  category: Category;
  createdAt: string;
}