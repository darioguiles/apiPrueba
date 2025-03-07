import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import Swal from 'sweetalert2';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit{

  usuarioForm!: FormGroup;
  usuario: any = null;
  nombreUsuarioExiste = false;
  correoExiste = false;
  constructor(private fb: FormBuilder, private authService: AuthService) {
  }
  ngOnInit(): void {
    this.cargarUsuario();
  }

  // Cargar usuarios desde el backend
  cargarUsuario() {
    const usuarioData = this.authService.obtenerUsuario();
    if (usuarioData) {
      this.usuario = usuarioData;
      this.usuarioForm = this.fb.group({
        nomUsuario: [this.usuario.nomUsuario],
        correo: [this.usuario.correo],
        telefono: [this.usuario.trabajador ? this.usuario.trabajador.telefono : this.usuario.empresa?.telefono, Validators.required],
        // Campos adicionales de Trabajador
        nombreTrabajador: [this.usuario.trabajador?.nombre || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        apellidosTrabajador: [this.usuario.trabajador?.apellidos || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        informacionTrabajador: [this.usuario.trabajador?.informacion || ''],
        // Campos adicionales de Empresa
        nombreEmpresa: [this.usuario.empresa?.nombre || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        descripcionEmpresa: [this.usuario.empresa?.descripcion || ''],
      });

      this.usuarioForm.get('nomUsuario')?.valueChanges.subscribe(value => this.verificarNombreUsuario(value));
      this.usuarioForm.get('correo')?.valueChanges.subscribe(value => this.verificarCorreo(value));
    }
  }

  get esTrabajador(): boolean {
    return !!this.usuario?.trabajador;
  }

  get esEmpresa(): boolean {
    return !!this.usuario?.empresa;
  }

  verificarNombreUsuario(nuevoNombre: string) {
    this.authService.verificarNombreUsuario(nuevoNombre).subscribe(existe => {
      this.nombreUsuarioExiste = existe;
    });
  }

  verificarCorreo(nuevoCorreo: string) {
    this.authService.verificarCorreo(nuevoCorreo).subscribe(existe => {
      this.correoExiste = existe;
    });
  }
  guardarCambios() {
    if (this.usuarioForm.invalid || this.nombreUsuarioExiste || this.correoExiste) return;

    const datosActualizados = { ...this.usuario, ...this.usuarioForm.value };

    // Si es trabajador, actualizar solo datos de trabajador
    if (this.esTrabajador) {
      datosActualizados.trabajador = {
        ...this.usuario.trabajador,
        nombre: this.usuarioForm.value.nombreTrabajador,
        apellidos: this.usuarioForm.value.apellidosTrabajador,
        informacion: this.usuarioForm.value.informacionTrabajador,
        telefono: this.usuarioForm.value.telefono,
      };
    }

    // Si es empresa, actualizar solo datos de empresa
    if (this.esEmpresa) {
      datosActualizados.empresa = {
        ...this.usuario.empresa,
        nombre: this.usuarioForm.value.nombreEmpresa,
        descripcion: this.usuarioForm.value.descripcionEmpresa,
        telefono: this.usuarioForm.value.telefono,
      };
    }

    this.authService.updateUsuario(this.usuario.id, datosActualizados).subscribe({
      next: (usuarioActualizado) => {
        Swal.fire('Éxito', 'Tus datos han sido actualizados.', 'success');
        this.authService.setUsuario(usuarioActualizado); // Actualiza localStorage y BehaviorSubject
      },
      error: () => Swal.fire('Error', 'Hubo un problema al actualizar los datos.', 'error')
    });
  }

  confirmarEliminarUsuario() {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará tu cuenta permanentemente.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eliminarUsuario();
      }
    });
  }
  eliminarUsuario() {
    const usuarioData = this.authService.obtenerUsuario();
    console.log("Usuario antes de eliminar:", usuarioData);
    if (!usuarioData || !usuarioData.idUsuario) {
      Swal.fire('Error', 'No se puede eliminar el usuario. ID no encontrado.', 'error');
      return;
    }

    this.authService.deleteUsuario(usuarioData.idUsuario).subscribe({
      next: () => {
        Swal.fire('Cuenta eliminada', 'Tu cuenta ha sido eliminada correctamente.', 'success');
        localStorage.removeItem('usuario');
        window.location.href = '/landing';
      },
      error: () => Swal.fire('Error', 'Hubo un problema al eliminar la cuenta.', 'error')
    });
  }
}
