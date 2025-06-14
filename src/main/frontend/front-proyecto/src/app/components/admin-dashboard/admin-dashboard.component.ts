import { Component, OnInit } from '@angular/core';
import { AnuncioService } from '../../services/anuncio.service';
import {GestionUserService} from "../../services/gestion-user.service";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-dashboard',
  standalone:true,
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  usuarios: any[] = [];
  anunciosEmpresa: any[] = [];
  anunciosTrabajador: any[] = [];

  constructor(
    private anuncioService: AnuncioService,
    private gestionUser: GestionUserService
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
    this.cargarAnunciosEmpresa();
    this.cargarAnunciosTrabajador();
  }

  confirmarEliminarAnuncio(anuncio: any) {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará el anuncio.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eliminarAnuncio(anuncio);
      }
    });
  }

  eliminarAnuncio(anuncio: any) {
    let id: number;
    let tipo: string;

    if (anuncio.idAnuncioEmpresa) {
      id = anuncio.idAnuncioEmpresa;
      tipo = 'empresa';
    } else {
      id = anuncio.idAnuncioTrabajador;
      tipo = 'trabajador';
    }

    this.anuncioService.borrarAnuncio(id, tipo).subscribe({
      next: () => {
        Swal.fire('Eliminado', 'El anuncio fue eliminado correctamente.', 'success');
        tipo === 'empresa' ? this.cargarAnunciosEmpresa() : this.cargarAnunciosTrabajador();
      },
      error: () => {
        Swal.fire('Error', 'No se pudo eliminar el anuncio.', 'error');
      }
    });
  }

  cargarUsuarios() {
    this.anuncioService.getAllUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  cargarAnunciosEmpresa() {
    this.anuncioService.getAllAnunciosEmpresa().subscribe(data => {
      this.anunciosEmpresa = data.anuncioEmpresa;
    });
  }

  cargarAnunciosTrabajador() {
    this.anuncioService.getAllAnunciosTrabajador().subscribe(data => {
      this.anunciosTrabajador = data.anuncioTrabajador;
    });
  }

  editarUsuario(usuario: any) {
    Swal.fire({
      title: 'Editar usuario',
      html:
          `<input id="swal-input1" class="swal2-input" placeholder="Correo" value="${usuario.correo}">` +
          `<input id="swal-input2" class="swal2-input" placeholder="Nombre usuario" value="${usuario.nombreUsuario}">`,
      focusConfirm: false,
      showCancelButton: true,
      preConfirm: () => {
        const correo = (document.getElementById('swal-input1') as HTMLInputElement).value;
        const nombreUsuario = (document.getElementById('swal-input2') as HTMLInputElement).value;

        if (!correo || !nombreUsuario) {
          Swal.showValidationMessage('Todos los campos son obligatorios');
          return false;
        }

        return { correo, nombreUsuario };
      }
    }).then(result => {
      if (result.isConfirmed) {
        const nuevosDatos = {
          ...usuario,
          correo: result.value.correo,
          nombreUsuario: result.value.nombreUsuario
        };

        this.gestionUser.updateUsuario(usuario.idUsuario, nuevosDatos).subscribe({
          next: () => {
            this.cargarUsuarios();
            Swal.fire('Actualizado', 'El usuario fue actualizado correctamente.', 'success');
          },
          error: () => {
            Swal.fire('Error', 'No se pudo actualizar el usuario.', 'error');
          }
        });
      }
    });
  }

  eliminarUsuario(id: number) {
    if (confirm('¿Seguro que deseas eliminar este usuario?')) {
      this.gestionUser.deleteUsuario(id).subscribe(() => {
        this.cargarUsuarios();
      });
    }
  }

  editarAnuncio(anuncio: any) {
    Swal.fire({
      title: 'Editar descripción del anuncio',
      input: 'text',
      inputLabel: 'Nueva descripción',
      inputValue: anuncio.descripcion,
      showCancelButton: true,
      confirmButtonText: 'Guardar',
      preConfirm: (desc) => {
        if (!desc || desc.trim().length < 5) {
          Swal.showValidationMessage('La descripción debe tener al menos 5 caracteres');
          return false;
        }
        return desc;
      }
    }).then((result) => {
      if (result.isConfirmed) {
        anuncio.descripcion = result.value;

        const id = anuncio.idAnuncioEmpresa || anuncio.idAnuncioTrabajador;
        this.anuncioService.editarAnuncio(id, anuncio).subscribe({
          next: () => {
            Swal.fire('Éxito', 'El anuncio fue actualizado.', 'success');
          },
          error: () => {
            Swal.fire('Error', 'No se pudo actualizar el anuncio.', 'error');
          }
        });
      }
    });
  }



}
