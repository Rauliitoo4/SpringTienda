import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) },
  { path: 'catalogo', loadComponent: () => import('./pages/catalog/catalog.component').then(m => m.CatalogComponent) },
  { path: 'producto/:id', loadComponent: () => import('./pages/product-detail/product-detail.component').then(m => m.ProductDetailComponent) },
  { path: 'nosotros', loadComponent: () => import('./pages/about/about.component').then(m => m.AboutComponent) },
  { path: 'contacto', loadComponent: () => import('./pages/contact/contact.component').then(m => m.ContactComponent) },
  { path: 'auth', loadComponent: () => import('./pages/auth/auth.component').then(m => m.AuthComponent) },
  { path: 'carrito', loadComponent: () => import('./pages/carrito/carrito.component').then(m => m.CarritoComponent), canActivate: [authGuard] },
  { path: 'favoritos', loadComponent: () => import('./pages/favorites/favorites.component').then(m => m.FavoritesComponent), canActivate: [authGuard] },
  { path: 'perfil', loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent), canActivate: [authGuard] },
];
