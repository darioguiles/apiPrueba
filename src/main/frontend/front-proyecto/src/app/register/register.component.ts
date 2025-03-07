import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {CommonModule, NgIf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import Swal from "sweetalert2";

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [
    ReactiveFormsModule,
    NgIf,
    CommonModule, FormsModule, HttpClientModule
  ],
  styleUrl: './register.component.scss',
  providers: [AuthService]
})
export class RegisterComponent {
  registerForm: FormGroup;
  perfilSeleccionado: string = '';
  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      nombreUsuario: ['', [Validators.required, Validators.minLength(3), Validators.pattern(/^[a-zA-Z0-9]+$/)]],
      correo: ['', [Validators.required, Validators.email, Validators.pattern(/^[\w._%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/)]],
      contrasenia: ['', [Validators.required, Validators.minLength(6)]],
      perfil: ['', Validators.required],
      nombreTrabajador: ['', Validators.pattern(/^[a-zA-Z\s]+$/)],
      apellidosTrabajador: ['', Validators.pattern(/^[a-zA-Z\s]+$/)],
      informacionTrabajador: ['', Validators.pattern(/^[a-zA-Z0-9\s.,!]+$/)],
      telefonoTrabajador: ['', [Validators.pattern(/^[0-9]{9}$/)]],
      nombreEmpresa: ['', Validators.pattern(/^[a-zA-Z0-9\s.,!]+$/)],
      descripcionEmpresa: ['', Validators.pattern(/^[a-zA-Z0-9\s.,!]+$/)],
      telefonoEmpresa: ['', [Validators.pattern(/^[0-9]{9}$/)]],
    });
  }

  onPerfilChange(): void {
    this.perfilSeleccionado = this.registerForm.get('perfil')?.value;
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      Swal.fire('Error', 'Revisa los campos antes de enviar.', 'error');
      return;
    }

    const formValue = this.registerForm.value;

    /*



    this.authService.register(usuario).subscribe({
      next: (usuarioCreado) => {
        console.log('Usuario y perfil creados correctamente:', usuarioCreado);
      },
      error: (error) => console.error('Error al registrar el usuario:', error)
    });
    */
    // Verificar si el usuario o el correo ya existen antes de registrar
    this.authService.verificarNombreUsuario(formValue.nombreUsuario).subscribe(existeNombre => {
      if (existeNombre) {
        Swal.fire('Error', 'Este nombre de usuario ya está en uso.', 'error');
        return;
      }

      this.authService.verificarCorreo(formValue.correo).subscribe(existeCorreo => {
        if (existeCorreo) {
          Swal.fire('Error', 'Este correo ya está en uso.', 'error');
          return;
        }

        // Si ni el nombre ni el correo existen, procedemos con el registro
        const usuario : any = {
          nomUsuario: formValue.nombreUsuario,
          correo: formValue.correo,
          contrasenia: formValue.contrasenia,
          rutapfp: '',
          esAdmin: false,
          trabajador: null,
          empresa: null
        };

        if (this.perfilSeleccionado === 'trabajador') {
          usuario.trabajador = {
            nombre: formValue.nombreTrabajador,
            apellidos: formValue.apellidosTrabajador,
            informacion: formValue.informacionTrabajador,
            telefono: formValue.telefonoTrabajador
          };
        } else if (this.perfilSeleccionado === 'empresa') {
          usuario.empresa = {
            nombre: formValue.nombreEmpresa,
            descripcion: formValue.descripcionEmpresa,
            telefono: formValue.telefonoEmpresa
          };
        }

        this.authService.register(usuario).subscribe({
          next: (response) => {
            Swal.fire('Éxito', 'Usuario registrado correctamente. Redirigiendo al login...', 'success');
            setTimeout(() => this.router.navigate(['/login']), 2000);
          },
          error: (error) => {
            console.error('Error al registrar el usuario:', error);
            Swal.fire('Error', 'Hubo un problema al registrar el usuario.', 'error');
          }
        });
      });
    });
  }
}
