import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {httpInterceptorProviders} from "interceptor/http.interceptor";
import {ContenidoComponent} from "./contenido/contenido.component";
import {LandingComponent} from "./landing/landing.component";
import {NavegadorComponent} from "./navegador/navegador.component";
// import {AuthGuard} from "./security/authguard";

@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ContenidoComponent,
    LandingComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
  ],
  providers: [httpInterceptorProviders
    // , AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
