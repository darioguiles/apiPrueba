import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {CommonModule, NgIf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";

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
  registroForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Inicializar formulario
    this.registroForm = this.fb.group({
      nombreUsuario: ['', [Validators.required,]],
      correo: ['', [Validators.required, Validators.email]],
      contrasenia: ['', [Validators.required, Validators.minLength(6)]],
      esAdmin: [false],
    });
  }

  // Método para manejar el envío del formulario
  onSubmit(): void {
    if (this.registroForm.valid) {
      const formValues = this.registroForm.value;

      const usuario = {
        nombreUsuario: formValues.nombreUsuario,
        correo: formValues.correo,
        contrasenia: formValues.contrasenia,
        esAdmin:formValues.esAdmin
      }


      // Enviar los datos al backend
      this.authService.register(usuario).subscribe({
        next: () => {
          alert('Usuario registrado exitosamente.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Error en el registro:', err);
          alert('Hubo un error al registrar. Inténtalo de nuevo.');
        },
      });
    }
  }
}
