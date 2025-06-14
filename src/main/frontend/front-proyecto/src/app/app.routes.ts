import { Routes } from '@angular/router';
import {LandingComponent} from "./landing/landing.component";
import {LoginComponent} from "./login/login.component";
import {ContenidoComponent} from "./contenido/contenido.component";
import {RegisterComponent} from "./register/register.component";
import {UsuariosComponent} from "./components/usuarios/usuarios.component";
import {AdminDashboardComponent} from "./components/admin-dashboard/admin-dashboard.component";

export const routes: Routes = [
    {path:'landing', component:LandingComponent},
    {path:'login', component: LoginComponent},
    {path:'content', component: ContenidoComponent},
    {path:'register', component: RegisterComponent},
  {path:'usuario', component: UsuariosComponent},
  { path: 'admin', component: AdminDashboardComponent },
  {path: '**', pathMatch: 'full', redirectTo: 'landing'}
];
