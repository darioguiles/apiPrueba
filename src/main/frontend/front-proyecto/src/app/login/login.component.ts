import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../services/auth.service";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CommonModule, NgIf} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, NgIf, CommonModule, FormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [AuthService]
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      nomUsuarioCorreo: ['', [Validators.required]],
      contrasenia: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  // Método para manejar el envío del formulario
  onSubmit(): void {
    if (this.loginForm.valid) {
      const formData = this.loginForm.value;
      const loginRequest = {
        nomUsuarioCorreo: formData.nomUsuarioCorreo,  // Puede ser correo o nombre de usuario
        password: formData.contrasenia,
      };

      this.authService.login(loginRequest.nomUsuarioCorreo, loginRequest.password).subscribe({
        next: (usuario) => {
          // Guardar el usuario en el localStorage
          localStorage.setItem('usuario', JSON.stringify(usuario));
          this.router.navigate(['/']); // Redirigir al inicio tras login
        },
        error: (err) => {
          console.error('Error en el login:', err);
          alert('Credenciales inválidas');
        },
      });
    }
  }

}
