import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-navegador',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgIf],
  templateUrl: './navegador.component.html',
  styleUrl: './navegador.component.scss'
})
export class NavegadorComponent {

  usuario: any = null;

  ngOnInit(): void {
    // Obtener los datos del usuario logueado desde localStorage
    const usuarioData = localStorage.getItem('usuario');
    if (usuarioData) {
      this.usuario = JSON.parse(usuarioData);
    }
  }

  logout(): void {
    // Cerrar sesión eliminando datos del usuario
    localStorage.removeItem('usuario');
    this.usuario = null;
  }
}
