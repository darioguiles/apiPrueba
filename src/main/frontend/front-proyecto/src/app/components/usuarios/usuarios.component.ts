import { Component, OnInit } from '@angular/core';
import {gestionUser} from "../../services/gestionUser";

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit{

  usuarios: any[] = []; // Lista de usuarios con relaciones
  constructor(private usuarioService: gestionUser) {}
  ngOnInit(): void {
    this.cargarUsuarios();
  }

  // Cargar usuarios desde el backend
  cargarUsuarios(): void {
    this.usuarioService.getUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
      },
    });
  }

  // Eliminar un usuario
  eliminarUsuario(id: number): void {
    this.usuarioService.deleteUsuario(id).subscribe({
      next: () => this.cargarUsuarios(), // Recargar lista tras eliminar
      error: (error) => {
        console.error('Error al eliminar usuario:', error);
      },
    });
  }
}
