import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/v1/data-api';

  constructor(private http: HttpClient) {}

  login(userOrCorreo: string, contrasenia: string): Observable<any> {
    console.log(userOrCorreo, contrasenia)
    return this.http.post<any>(`${this.apiUrl}/usuarios/login`,
      {
        nomUsuarioCorreo: userOrCorreo,  // Se envía como nombre de usuario o correo
        contrasenia: contrasenia
      },
      {
      headers: { 'Content-Type': 'application/json' }
    });

  }

  register(usuario: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/usuarios/register`, usuario, {
      headers: { 'Content-Type': 'application/json' }  // Asegúrate de que el Content-Type esté configurado
    });
  }

}
