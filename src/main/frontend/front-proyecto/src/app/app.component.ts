import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {NavegadorComponent} from "./navegador/navegador.component";
import {AuthService} from "./services/auth.service";
import {AnuncioService} from "./services/anuncio.service";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavegadorComponent, RouterOutlet, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  providers: [AuthService, AnuncioService]
})
export class AppComponent {
  title = 'front-proyecto';
}
