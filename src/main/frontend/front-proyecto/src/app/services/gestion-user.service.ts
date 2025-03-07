import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GestionUserService {
  private apiUrl = 'http://localhost:8080/v1/data-api/usuarios'; // URL base del backend

  /**
   * Se abandona esta clase para meterlo en el servicio de Auth, dado que así
   */
  constructor(private http: HttpClient) {}

  // Obtener todos los usuarios
  getUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  //Obtener un usuario
  getOneUsuario(id:number):Observable<any[]>{
    return this.http.get<any[]>(`${this.apiUrl}/${id}`);
  }

  // Eliminar un usuario
  deleteUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Actualizar un usuario !!!TODO
  updateUsuario(id: number, usuario: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, usuario);
  }
}
