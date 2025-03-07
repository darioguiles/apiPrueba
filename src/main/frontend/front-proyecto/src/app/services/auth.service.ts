import {EventEmitter, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/v1/data-api';
  private usuarioSubject = new BehaviorSubject<any>(null); // Use BehaviorSubject
  usuario$ = this.usuarioSubject.asObservable(); // Expose as observable

  constructor(private http: HttpClient) {
    // Initialize with the user from localStorage (if any)
    const usuarioData = localStorage.getItem('usuario');
    if (usuarioData) {
      this.usuarioSubject.next(JSON.parse(usuarioData));
    }
  }


  //Funciona!! Es un Login el cual te permite loguearte segun tu nomUsuario o Correo.
  login(userOrCorreo: string, contrasenia: string): Observable<any> {
    //console.log(userOrCorreo, contrasenia)
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


  //Metodos para cuando se actualize el user/correo
  verificarNombreUsuario(nomUsuario: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/usuarios/verificar-nombre?nomUsuario=${nomUsuario}`);
  }

  verificarCorreo(correo: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/usuarios/verificar-correo?correo=${correo}`);
  }
  // Method to update user state
  setUsuario(usuario: any) {
    //console.log('Setting usuario:', usuario); // Debugging
    localStorage.setItem('usuario', JSON.stringify(usuario));
    this.usuarioSubject.next(usuario); // Emit the new user
  }

  obtenerUsuario() : any{
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : null;
  }

  // Method to clear user state
  clearUsuario() {
    console.log('Clearing usuario'); // Debugging
    localStorage.removeItem('usuario');
    this.usuarioSubject.next(null); // Emit null to indicate logout
  }


  deleteUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/usuarios/${id}`);
  }

  // Actualizar un usuario !!Atencion cuidado esto es DB lo otro es LocalStorage
  updateUsuario(id: number, usuario: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/usuarios/${id}`, usuario);
  }
}
