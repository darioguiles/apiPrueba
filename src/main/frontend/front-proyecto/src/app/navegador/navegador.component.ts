import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {NgIf} from "@angular/common";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-navegador',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, NgIf],
  templateUrl: './navegador.component.html',
  styleUrl: './navegador.component.scss'
})
export class NavegadorComponent implements OnInit{

  usuario: any = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Obtener los datos del usuario logueado desde localStorage
    const usuarioData = localStorage.getItem('usuario');
    if (usuarioData) {
      this.usuario = JSON.parse(usuarioData);
    }

    console.log('NavegadorComponent initialized'); // Debugging

    // Subscribe to user changes
    this.authService.usuario$.subscribe((usuario) => {
      console.log('Usuario changed in Nav:', usuario); // Debugging
      this.usuario = usuario;
    });

    // Subscribe to route changes
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Update the usuario state whenever the route changes
        const usuarioData = localStorage.getItem('usuario');
        this.usuario = usuarioData ? JSON.parse(usuarioData) : null;
      }
    });
  }


  logout(): void {
    console.log('Logging out...'); // Debugging
    this.authService.clearUsuario(); // Clear user state
    this.router.navigate(['/landing']); // Redirect to landing page
  }
}
