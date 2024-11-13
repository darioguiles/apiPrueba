import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {ContenidoComponent} from "./contenido/contenido.component";
import {LandingComponent} from "./landing/landing.component";
import {NavegadorComponent} from "./navegador/navegador.component";
// import {AuthGuard} from "./security/authguard";

@NgModule({
  declarations: [
    RegisterComponent,
    ContenidoComponent,
    LandingComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
  ],

  bootstrap: [AppComponent]
})
export class AppModule { }
