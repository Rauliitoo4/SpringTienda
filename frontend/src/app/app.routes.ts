import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProductComponent } from './pages/product/product.component';
import { TiendasComponent } from './pages/tiendas/tiendas.component';
import { CarritoComponent } from './pages/carrito/carrito.component';

export const routes: Routes = [
    {path:'', component: HomeComponent},
    {path:'tiendas', component: TiendasComponent},
    {path:'product', component: ProductComponent},
    {path:'carrito', component: CarritoComponent}
];